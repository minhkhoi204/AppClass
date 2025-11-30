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

        // Validate promise dates with role
        validatePromiseDatesWithRole(dto.getRole(), dto.getAssistantCatechistPromiseDate(), 
                                     dto.getCatechistPromiseDate(), dto.getDateOfBirth());

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

        // Validate promise dates with role
        validatePromiseDatesWithRole(dto.getRole(), dto.getAssistantCatechistPromiseDate(), 
                                     dto.getCatechistPromiseDate(), user.getDateOfBirth());

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

        // Determine final values for validation (merge request with existing data)
        Role finalRole = requestDto.getRole() != null ? requestDto.getRole() : catechist.getRole();
        LocalDate finalAssistantDate = requestDto.getAssistantCatechistPromiseDate() != null ? 
                requestDto.getAssistantCatechistPromiseDate() : catechist.getAssistantCatechistPromiseDate();
        LocalDate finalCatechistDate = requestDto.getCatechistPromiseDate() != null ? 
                requestDto.getCatechistPromiseDate() : catechist.getCatechistPromiseDate();
        
        // Validate promise dates with role (this ensures data integrity)
        validatePromiseDatesWithRole(finalRole, finalAssistantDate, finalCatechistDate, user.getDateOfBirth());

        // update catechist
        if (requestDto.getRole() != null) {
            catechist.setRole(requestDto.getRole());
            catechist.setIsExecutiveBoard(RoleUtils.isExecutiveBoard(requestDto.getRole()));

            // update role of user
            user.setRole(requestDto.getRole());
        }
        
        if (requestDto.getAssistantCatechistPromiseDate() != null) {
            catechist.setAssistantCatechistPromiseDate(requestDto.getAssistantCatechistPromiseDate());
        }
        
        if (requestDto.getCatechistPromiseDate() != null) {
            catechist.setCatechistPromiseDate(requestDto.getCatechistPromiseDate());
        }
        
        if (requestDto.getNote() != null) {
            catechist.setNote(requestDto.getNote());
        }
        
        if (requestDto.getClassroomId() != null) {
            catechist.setClassroomId(requestDto.getClassroomId());
        }

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


    private void validatePromiseDatesWithRole(Role role, LocalDate assistantPromiseDate, 
                                              LocalDate catechistPromiseDate, LocalDate dateOfBirth) {
        // Rule 1: DU_TRUONG validation
        if (role == Role.DU_TRUONG) {
            // assistantCatechistPromiseDate is optional for DU_TRUONG
            // catechistPromiseDate MUST be null for DU_TRUONG
            if (catechistPromiseDate != null) {
                throw new IllegalArgumentException(
                    "Assistant Catechist (DU_TRUONG) cannot have catechistPromiseDate. " +
                    "Use role HUYNH_TRUONG if already promoted."
                );
            }
            
            // If assistantPromiseDate is provided, validate it
            if (assistantPromiseDate != null) {
                validatePromiseDate(assistantPromiseDate, dateOfBirth, "assistantCatechistPromiseDate");
            }
            return;
        }

        // Rule 2: HUYNH_TRUONG and executive board roles validation
        // MUST have both promise dates
        if (assistantPromiseDate == null) {
            throw new IllegalArgumentException(
                "Role " + role + " requires assistantCatechistPromiseDate. " +
                "Must be DU_TRUONG first before becoming HUYNH_TRUONG."
            );
        }
        
        if (catechistPromiseDate == null) {
            throw new IllegalArgumentException(
                "Role " + role + " requires catechistPromiseDate. " +
                "Use role DU_TRUONG if not yet promoted."
            );
        }

        // Rule 3: Validate both dates
        validatePromiseDate(assistantPromiseDate, dateOfBirth, "assistantCatechistPromiseDate");
        validatePromiseDate(catechistPromiseDate, dateOfBirth, "catechistPromiseDate");

        // Rule 4: catechistPromiseDate must be after assistantCatechistPromiseDate
        if (catechistPromiseDate.isBefore(assistantPromiseDate)) {
            throw new IllegalArgumentException(
                "catechistPromiseDate must be after assistantCatechistPromiseDate. " +
                "Assistant promise: " + assistantPromiseDate + ", Catechist promise: " + catechistPromiseDate
            );
        }
    }

    private void validatePromiseDate(LocalDate promiseDate, LocalDate dateOfBirth, String fieldName) {
        // Cannot be in the future
        if (promiseDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(fieldName + " cannot be in the future");
        }

        // Must be at least 16 years old
        if (dateOfBirth != null) {
            LocalDate minPromiseDate = dateOfBirth.plusYears(16);
            if (promiseDate.isBefore(minPromiseDate)) {
                throw new IllegalArgumentException(
                    fieldName + " must be after turning 16 years old. Minimum date: " + minPromiseDate
                );
            }
        }
    }
}
