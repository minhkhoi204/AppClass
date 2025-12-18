package com.programming.common.common_dto.auth;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String userName;
    private String password;
    private String fullName;
    private String christianName;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
}
