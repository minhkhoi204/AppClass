package com.programming.common.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programming.common.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom Authentication Entry Point to handle authentication errors
 * and return JSON response with error details
 */
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        
        log.error("Authentication error: {}", authException.getMessage());
        
        // Set response status and content type
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Create error response
        ApiResponse errorResponse = new ApiResponse(
            authException.getMessage() != null ? authException.getMessage() : "Authentication failed",
            null
        );
        
        // Write response
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
