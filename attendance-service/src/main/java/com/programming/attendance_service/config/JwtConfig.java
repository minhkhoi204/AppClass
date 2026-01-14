package com.programming.attendance_service.config;

import com.programming.common.common_auth.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }
}
