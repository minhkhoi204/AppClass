package com.programming.management_service.dto;

import lombok.Getter;
import lombok.Setter;

// Trong cả user-service và management-service
@Getter
@Setter
public class UserDto {
    private Long id;
    private String fullName;
    private String saintName;

}
