package com.programming.user_service.service;

import com.programming.user_service.dto.UserRequestDto;
import com.programming.user_service.dto.UserResponseDto;
import com.programming.user_service.exception.AlreadyExistsException;
import com.programming.user_service.exception.ResourceNotFoundException;
import com.programming.user_service.mapper.UserMapper;
import com.programming.user_service.model.User;
import com.programming.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        // Check nếu username đã tồn tại
        if (userRepository.existsByUserName(dto.getUserName())) {
            throw new AlreadyExistsException("Username already exists");
        }

        // Check nếu fullName và saintName đã tồn tại
        if (userRepository.existsByFullNameAndSaintName(dto.getFullName(), dto.getSaintName())) {
            throw new AlreadyExistsException("Student already exists");
        }

        User user = userMapper.toUserEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // encode
        userRepository.save(user);
        return userMapper.toUserResponseDto(user);
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
