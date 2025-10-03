package com.programming.user_service.service;

import com.programming.user_service.dto.UserRequestDto;
import com.programming.user_service.dto.UserResponseDto;
import com.programming.user_service.model.User;

public interface UserService {
    UserResponseDto createUser(UserRequestDto dto);
    UserResponseDto getUserById(Long id);
    boolean userExists(Long id);

    UserRequestDto updateUser(Long id, UserRequestDto dto);
    UserResponseDto convertUserToDto(User user);


}
