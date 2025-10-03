package com.programming.user_service.mapper;

import com.programming.user_service.dto.UserRequestDto;
import com.programming.user_service.dto.UserResponseDto;
import com.programming.user_service.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDto toUserResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setFullName(user.getFullName());
        dto.setSaintName(user.getSaintName());
        return dto;
    }

    public User toUserEntity(UserRequestDto dto) {
        User user = new User();
        user.setUserName(dto.getUserName());
        user.setPassword(dto.getPassword());
        user.setFullName(dto.getFullName());
        user.setSaintName(dto.getSaintName());
        return user;
    }

    public void updateEntity(User user, UserRequestDto dto) {
        user.setUserName(dto.getUserName());
        user.setFullName(dto.getFullName());
        user.setSaintName(dto.getSaintName());
        // handle password later
    }
}
