package com.programming.user_service.service.catechist;

import com.programming.common.common_auth.Role;
import com.programming.common.common_auth.RoleUtils;
import com.programming.common.common_dto.catechist.CatechistRequestDto;
import com.programming.common.common_dto.catechist.CatechistResponseDto;
import com.programming.user_service.domain.model.Catechist;
import com.programming.user_service.domain.model.User;
import com.programming.common.exception.AlreadyExistsException;
import com.programming.common.exception.ResourceNotFoundException;
import com.programming.user_service.mapper.CatechistMapper;
import com.programming.user_service.repository.CatechistRepository;
import com.programming.user_service.repository.UserRepository;
import com.programming.user_service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatechistServiceImpl implements CatechistService {
    private final UserRepository userRepository;
    private final CatechistRepository catechistRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CatechistResponseDto createCatechist(CatechistRequestDto dto) {
        // Check if catechist exits (fullName + christianName)
        if (userRepository.existsByFullNameAndChristianName(dto.getFullName(), dto.getChristianName())) {
            throw new AlreadyExistsException("User already exists");
        }

        // create username automatically
        String generatedUsername = userService.generateUsername(dto.getFullName(), dto.getDateOfBirth());

        if (userRepository.existsByUserName(generatedUsername)) {
            throw new AlreadyExistsException("Username already exists: " + generatedUsername);
        }

        // check if role available (must be catechist role)
        if (dto.getRole() == null) {
            throw new IllegalArgumentException("Role is required for catechist");
        }
        if (!RoleUtils.isCatechist(dto.getRole())) {
            throw new IllegalArgumentException("Invalid role for catechist: " + dto.getRole());
        }

        // Validate promiseDate theo role
        validatePromiseDateWithRole(dto.getRole(), dto.getPromiseDate(), dto.getDateOfBirth());

        // create User
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setChristianName(dto.getChristianName());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setUserName(generatedUsername);
        user.setPassword(passwordEncoder.encode("defaultPassword")); // or random
        
        // set role of user
        user.setRole(dto.getRole());
        
        userRepository.save(user);

        // create Catechist and assign user
        Catechist catechist = CatechistMapper.toCatechistEntity(dto);
        catechist.setUser(user);
        catechist.setIsExecutiveBoard(RoleUtils.isExecutiveBoard(dto.getRole()));
        
        catechistRepository.save(catechist);

        return CatechistMapper.toCatechistDto(catechist);
    }

    @Override
    public CatechistResponseDto createCatechistWithUserId(CatechistRequestDto dto, Long userId) {
        // check user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // check catechist already exists for this user
        if (catechistRepository.existsByUserId(userId)) {
            throw new AlreadyExistsException("Catechist already exists for this user");
        }

        // check user already has role
        if (user.getRole() != null) {
            throw new IllegalStateException("User already has role.");
        }

        // check fullName and christianName match
        if (!user.getFullName().equals(dto.getFullName()) ||
                !user.getChristianName().equals(dto.getChristianName())) {
            throw new IllegalStateException("Provided fullName or christianName does not match with the user.");
        }

        // check if role available (must be catechist role)
        if (dto.getRole() == null) {
            throw new IllegalArgumentException("Role is required for catechist");
        }
        if (!RoleUtils.isCatechist(dto.getRole())) {
            throw new IllegalArgumentException("Invalid role for catechist: " + dto.getRole());
        }

        // Validate promiseDate theo role
        validatePromiseDateWithRole(dto.getRole(), dto.getPromiseDate(), user.getDateOfBirth());

        // create Catechist
        Catechist catechist = CatechistMapper.toCatechistEntity(dto);
        catechist.setUser(user);
        catechist.setIsExecutiveBoard(RoleUtils.isExecutiveBoard(dto.getRole()));
        
        catechistRepository.save(catechist);

        // update role of user
        user.setRole(dto.getRole());
        userRepository.save(user);

        return CatechistMapper.toCatechistDto(catechist);
    }

    @Override
    public CatechistResponseDto updateCatechist(Long id, CatechistRequestDto requestDto) {
        // find catechist
        Catechist catechist = catechistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catechist not found with id: " + id));

        // get current user
        User user = catechist.getUser();

        // check if new role is valid if changed
        if (requestDto.getRole() != null && !RoleUtils.isCatechist(requestDto.getRole())) {
            throw new IllegalArgumentException("Invalid role for catechist: " + requestDto.getRole());
        }

        // Determine final role and promiseDate for validation
        Role finalRole = requestDto.getRole() != null ? requestDto.getRole() : catechist.getRole();
        LocalDate finalPromiseDate = requestDto.getPromiseDate() != null ? requestDto.getPromiseDate() : catechist.getPromiseDate();
        
        // Validate promiseDate với role
        validatePromiseDateWithRole(finalRole, finalPromiseDate, user.getDateOfBirth());

        // update catechist
        if (requestDto.getRole() != null) {
            catechist.setRole(requestDto.getRole());
            catechist.setIsExecutiveBoard(RoleUtils.isExecutiveBoard(requestDto.getRole()));

            // update role of user
            user.setRole(requestDto.getRole());
        }
        
        if (requestDto.getPromiseDate() != null) {
            catechist.setPromiseDate(requestDto.getPromiseDate());
        }
        
        if (requestDto.getNote() != null) {
            catechist.setNote(requestDto.getNote());
        }

        catechistRepository.save(catechist);
        userRepository.save(user);

        return CatechistMapper.toCatechistDto(catechist);
    }

    @Override
    public CatechistResponseDto promiseCatechist(Long catechistId, LocalDate promiseDate) {
        // Tìm catechist
        Catechist catechist = catechistRepository.findById(catechistId)
                .orElseThrow(() -> new ResourceNotFoundException("Catechist not found with id: " + catechistId));

        // Kiểm tra catechist hiện tại phải là DU_TRUONG
        if (catechist.getRole() != Role.DU_TRUONG) {
            throw new IllegalStateException("Chỉ Dự Trưởng mới có thể tuyên hứa. Role hiện tại: " + catechist.getRole());
        }

        // Kiểm tra chưa có promiseDate
        if (catechist.getPromiseDate() != null) {
            throw new IllegalStateException("Catechist này đã tuyên hứa vào ngày: " + catechist.getPromiseDate());
        }

        // Validate promiseDate
        if (promiseDate == null) {
            throw new IllegalArgumentException("Ngày tuyên hứa không được để trống");
        }
        
        // Validate promiseDate với role HUYNH_TRUONG
        validatePromiseDateWithRole(Role.HUYNH_TRUONG, promiseDate, catechist.getUser().getDateOfBirth());

        // Cập nhật promiseDate và chuyển role
        catechist.setPromiseDate(promiseDate);
        catechist.setRole(Role.HUYNH_TRUONG);
        
        // Cập nhật role của user
        User user = catechist.getUser();
        user.setRole(Role.HUYNH_TRUONG);
        
        catechistRepository.save(catechist);
        userRepository.save(user);

        return CatechistMapper.toCatechistDto(catechist);
    }

    @Override
    public void deleteCatechist(Long id) {
        catechistRepository.deleteById(id);
    }

    @Override
    public CatechistResponseDto getCatechistById(Long id) {
        Optional<Catechist> catechist = catechistRepository.findById(id);
        return catechist.map(CatechistMapper::toCatechistDto).orElse(null);
    }

    @Override
    public List<CatechistResponseDto> getAllCatechists() {
        return catechistRepository.findAll().stream()
                .map(CatechistMapper::toCatechistDto)
                .collect(Collectors.toList());
    }

    /**
     * Validate promiseDate với role
     * Rules:
     * 1. DU_TRUONG không được có promiseDate (chưa tuyên hứa)
     * 2. Các role khác (HUYNH_TRUONG, DOAN_TRUONG, ...) BẮT BUỘC phải có promiseDate
     * 3. promiseDate không được là tương lai
     * 4. promiseDate phải sau khi đủ 16 tuổi
     */
    private void validatePromiseDateWithRole(Role role, LocalDate promiseDate, LocalDate dateOfBirth) {
        // Rule 1: DU_TRUONG không được có promiseDate
        if (role == Role.DU_TRUONG) {
            if (promiseDate != null) {
                throw new IllegalArgumentException(
                    "Dự Trưởng không thể có ngày tuyên hứa. Nếu đã tuyên hứa, role phải là HUYNH_TRUONG hoặc cao hơn."
                );
            }
            return; // Valid DU_TRUONG
        }

        // Rule 2: Các role khác BẮT BUỘC phải có promiseDate
        if (promiseDate == null) {
            throw new IllegalArgumentException(
                "Role " + role + " yêu cầu phải có ngày tuyên hứa. Nếu chưa tuyên hứa, hãy sử dụng role DU_TRUONG."
            );
        }

        // Rule 3: promiseDate không được là tương lai
        if (promiseDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                "Ngày tuyên hứa không thể là ngày trong tương lai."
            );
        }

        // Rule 4: promiseDate phải sau khi đủ 16 tuổi
        if (dateOfBirth != null) {
            LocalDate minPromiseDate = dateOfBirth.plusYears(16);
            if (promiseDate.isBefore(minPromiseDate)) {
                throw new IllegalArgumentException(
                    "Ngày tuyên hứa phải sau khi đủ 16 tuổi. Ngày tuyên hứa sớm nhất: " + minPromiseDate
                );
            }
        }
    }
}
