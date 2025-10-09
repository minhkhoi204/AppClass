package com.programming.user_service.controller;

import com.programming.user_service.domain.dto.response.StudentResponseDto;
import com.programming.user_service.domain.dto.request.StudentRequestDto;
import com.programming.user_service.domain.model.Student;
import com.programming.user_service.response.ApiResponse;
import com.programming.user_service.service.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/students")

public class StudentController {

    private final StudentService studentService;

    @PostMapping("/create")
    public ResponseEntity<StudentResponseDto> createStudent(@RequestBody StudentRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(dto));
    }

}
