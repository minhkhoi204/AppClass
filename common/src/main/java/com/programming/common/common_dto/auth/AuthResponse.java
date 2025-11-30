package com.programming.common.common_dto.auth;

import com.programming.common.common_auth.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse { // DTO return after successfully login or registration
    private String token;
    private String username;
    private Long userId;
    private Role role;
    private String fullName;
    private String christianName;
    private String message;
}
