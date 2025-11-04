package com.programming.user_service.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequestDto {
    private Long id;

    //@NotBlank(message = "Password is required")
    private String password;

    //@NotBlank(message = "Full name is required")
    private String fullName;

    //@NotBlank(message = "Christian name is required")
    private String christianName;

    //@NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    private String userName;

    private String email;

    private String phone;
}
