package com.programming.management_service.handler;

import com.programming.management_service.exception.AlreadyExistsException;
import com.programming.management_service.exception.ResourceNotFoundException;
import com.programming.management_service.response.ApiResponse;
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
    public ResponseEntity<ApiResponse> handleResourceNotFound(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }

    @ExceptionHandler({AlreadyExistsException.class})
    public ResponseEntity<ApiResponse> handleAlreadyExists(AlreadyExistsException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
    }

    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<ApiResponse> handleIllegalState(IllegalStateException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneral(Exception e) {
        log.error("Unhandled error: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Unexpected error occurred", null));
    }
}
