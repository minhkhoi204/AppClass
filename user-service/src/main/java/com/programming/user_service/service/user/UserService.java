package com.programming.user_service.service.user;

import com.programming.user_service.domain.dto.request.UserRequestDto;
import com.programming.user_service.domain.dto.response.UserResponseDto;
import com.programming.user_service.domain.model.User;

import java.time.LocalDate;

public interface UserService {
    UserResponseDto createUser(UserRequestDto dto);
    UserResponseDto getUserById(Long id);
    boolean userExists(Long id);
    String generateUsername(String fullName, LocalDate dob);

    UserResponseDto updateUser(Long id, UserRequestDto dto);
    UserResponseDto convertUserToDto(User user);

    void deleteUser(Long id);
}
