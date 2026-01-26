package com.programming.management_service.management_caller;

import com.programming.common.common_dto.student.StudentRequestDto;
import com.programming.common.common_dto.student.StudentResponseDto;

import com.programming.common.response.ApiResponse;
import com.programming.management_service.config.FeignConfig;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@FeignClient(
        name = "user-service",
        url = "${user.service.url}",
        contextId = "studentClient",
        configuration = FeignConfig.class
)
public interface StudentClient {

    @PostMapping("/api/students/create")
    StudentResponseDto createStudent(@RequestBody StudentRequestDto studentRequestDto);

    @GetMapping("/api/students/{id}")
    ApiResponse getStudentById(@PathVariable("id") Long id);

    @GetMapping("/api/users/batch")
    List<StudentResponseDto> getStudentsByIds(@RequestParam("ids") Set<Long> ids);

    @PutMapping("/api/students/{id}/update")
    StudentResponseDto updateStudent(@PathVariable("id") Long studentId,
                                     @RequestBody StudentRequestDto updatedStudentDto);

//    @PutMapping("/api/students/{studentId}/classroom")
//    void updateStudentClassroom(@PathVariable("studentId") Long studentId,
//                                @RequestParam("classroomId") Long classroomId);
}


