package com.programming.user_service.service;

import com.programming.user_service.dto.UserRequestDto;
import com.programming.user_service.dto.UserResponseDto;
import com.programming.user_service.exception.ResourceNotFoundException;
import com.programming.user_service.mapper.UserMapper;
import com.programming.user_service.model.User;
import com.programming.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
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
    public UserRequestDto updateUser(Long id, UserRequestDto dto) {
        return null;
    }

    @Override
    public UserResponseDto convertUserToDto(User user) {
        return userMapper.toUserResponseDto(user);
    }
}
