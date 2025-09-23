package com.programming.user_service.handler;

import com.programming.user_service.exceptions.ResourceNotFoundException;
import com.programming.user_service.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
* slf4j -> log
* controller advice -> cho phép bắt exception ở mọi nơi trong project
*/
@Slf4j
@ControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ApiResponse> handleException(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
}
