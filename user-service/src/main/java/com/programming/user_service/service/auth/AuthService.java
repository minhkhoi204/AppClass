package com.programming.user_service.service.auth;

import com.programming.common.common_auth.JwtUtil;
import com.programming.common.common_auth.Role;
import com.programming.common.common_dto.auth.AuthResponse;
import com.programming.common.common_dto.auth.LoginRequest;
import com.programming.common.common_dto.auth.RegisterRequest;
import com.programming.common.exception.AlreadyExistsException;
import com.programming.user_service.config.CustomUserDetails;
import com.programming.user_service.domain.model.User;
import com.programming.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * Login user and generate JWT token
     */
    public AuthResponse login(LoginRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Get user details
            // getPrincipal() returns an Object, need to cast it to CustomUserDetails
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            // Generate JWT token
            String token = jwtUtil.generateToken(
                    user.getUserName(),
                    user.getId(),
                    user.getRole()
            );

            // Build response
            return AuthResponse.builder()
                    .token(token)
                    .username(user.getUserName())
                    .userId(user.getId())
                    .role(user.getRole())
                    .fullName(user.getFullName())
                    .christianName(user.getChristianName())
                    .message("Login successful")
                    .build();

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    /**
     * Register new user (THIEU_NHI only - basic registration)
     * For catechists, use the existing CatechistService
     */
    public AuthResponse register(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.existsByUserName(request.getUsername())) {
            throw new AlreadyExistsException("Username already exists: " + request.getUsername());
        }

        // Check if user with same fullName and christianName exists
        if (userRepository.existsByFullNameAndChristianName(
                request.getFullName(), 
                request.getChristianName())) {
            throw new AlreadyExistsException("User with this name already exists");
        }

        // Create new user with THIEU_NHI role (default for registration)
        User user = User.builder()
                .userName(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .christianName(request.getChristianName())
                .dateOfBirth(request.getDateOfBirth())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(Role.BASIC_USER) // Default role for self-registration
                .build();

        User savedUser = userRepository.save(user);

        // Generate token
        String token = jwtUtil.generateToken(
                savedUser.getUserName(),
                savedUser.getId(),
                savedUser.getRole()
        );

        return AuthResponse.builder()
                .token(token)
                .username(savedUser.getUserName())
                .userId(savedUser.getId())
                .role(savedUser.getRole())
                .fullName(savedUser.getFullName())
                .christianName(savedUser.getChristianName())
                .message("Registration successful")
                .build();
    }

    /**
     * Validate JWT token
     */
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    /**
     * Extract username from token
     */
    public String getUsernameFromToken(String token) {
        return jwtUtil.extractUsername(token);
    }
}
