package com.programming.management_service.management_caller;

import com.programming.common_dto.student.StudentRequestDto;
import com.programming.common_dto.student.StudentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "user-service",
        path = "/students",
        contextId = "studentClient"
)
public interface StudentClient {

    @PostMapping("/create")
    StudentResponseDto createStudent(@RequestBody StudentRequestDto studentRequestDto);

    @GetMapping("/{id}")
    StudentResponseDto getStudentById(@PathVariable("id") Long id);
}


