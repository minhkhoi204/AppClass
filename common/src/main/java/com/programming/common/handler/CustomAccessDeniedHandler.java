package com.programming.common.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programming.common.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom Access Denied Handler to handle authorization errors
 * (user is authenticated but doesn't have permission)
 * and return JSON response with error details
 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
                      HttpServletResponse response,
                      AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        log.error("Access denied: {}", accessDeniedException.getMessage());
        
        // Set response status and content type
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Create error response
        ApiResponse errorResponse = new ApiResponse(
            "Access denied: " + (accessDeniedException.getMessage() != null 
                ? accessDeniedException.getMessage() 
                : "You don't have permission to access this resource"),
            null
        );
        
        // Write response
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
