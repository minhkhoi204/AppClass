package com.programming.user_service.service.user;

import com.programming.user_service.domain.dto.request.UserRequestDto;
import com.programming.user_service.domain.dto.response.UserResponseDto;
import com.programming.common.exception.AlreadyExistsException;
import com.programming.common.exception.ResourceNotFoundException;
import com.programming.user_service.mapper.UserMapper;
import com.programming.user_service.domain.model.User;
import com.programming.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        // Check nếu fullName và saintName đã tồn tại
        if (userRepository.existsByFullNameAndChristianName(dto.getFullName(), dto.getChristianName())) {
            throw new AlreadyExistsException("User already exists");
        }

        // create username automatically
        //String generatedUsername = generateUsername(dto.getFullName(), dto.getDateOfBirth());

        // Check username exists
//        if (userRepository.existsByUserName(generatedUsername)) {
//            throw new AlreadyExistsException("Generated username already exists: " + generatedUsername);
//        }
        if (userRepository.existsByUserName(dto.getUserName())) {
            throw new AlreadyExistsException("Generated username already exists: " + dto.getUserName());
        }

        // create user entity from dto
        User user = userMapper.toUserEntity(dto);

        //user.setUserName(generatedUsername);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return userMapper.toUserResponseDto(user);
    }

    public String generateUsername(String fullName, LocalDate dob) {
        //normalize vietnamese
        String normalized = Normalizer.normalize(fullName, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .toLowerCase();

        //take the first characters
        StringBuilder initials = new StringBuilder();
        for (String part : normalized.trim().split("\\s+")) {
            if (!part.isEmpty()) {
                initials.append(part.charAt(0));
            }
        }

        //take date month and last 2 numbs
        String day = String.format("%02d", dob.getDayOfMonth());
        String month = String.format("%02d", dob.getMonthValue());
        String year = String.valueOf(dob.getYear()).substring(2);

        return initials.toString() + day + month + year;
    }


    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toUserResponseDto(user);
    }

    @Override
    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!user.getUserName().equals(dto.getUserName()) &&
                userRepository.existsByUserName(dto.getUserName())) {
            throw new AlreadyExistsException("Username already exists: " + dto.getUserName());
        }
        // update information from dto
        userMapper.updateEntity(user, dto);

        // save to db
        User updatedUser = userRepository.save(user);

        // return to responsedto
        return userMapper.toUserResponseDto(updatedUser);
    }

    @Override
    public UserResponseDto convertUserToDto(User user) {
        return userMapper.toUserResponseDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
