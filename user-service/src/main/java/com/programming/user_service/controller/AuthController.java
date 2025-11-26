package com.programming.user_service.controller;

import com.programming.common.common_dto.auth.AuthResponse;
import com.programming.common.common_dto.auth.LoginRequest;
import com.programming.common.common_dto.auth.RegisterRequest;
import com.programming.common.response.ApiResponse;
import com.programming.user_service.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Login endpoint
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(new ApiResponse("Login successful", response));
    }

    /**
     * Register endpoint (for THIEU_NHI only)
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(new ApiResponse("Registration successful", response));
    }

    /**
     * Test endpoint to verify authentication is working
     * GET /api/auth/test
     */
    @GetMapping("/test")
    public ResponseEntity<ApiResponse> test() {
        return ResponseEntity.ok(
                new ApiResponse("Authentication system is working!", "Public endpoint - no auth required")
        );
    }
}
