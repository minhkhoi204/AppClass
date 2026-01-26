package com.programming.user_service.service.user;

import com.programming.user_service.domain.dto.request.UserRequestDto;
import com.programming.user_service.domain.dto.response.UserResponseDto;
import com.programming.user_service.domain.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface UserService {
    UserResponseDto createUser(UserRequestDto dto);
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getUsersByIds(Set<Long> ids);
    boolean userExists(Long id);
    String generateUsername(String fullName, LocalDate dob);

    UserResponseDto updateUser(Long id, UserRequestDto dto);
    UserResponseDto convertUserToDto(User user);

    void deleteUser(Long id);
}
