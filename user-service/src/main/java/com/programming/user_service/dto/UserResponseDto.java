package com.programming.user_service.dto;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String userName;
    private String fullName;
    private String saintName;
}
