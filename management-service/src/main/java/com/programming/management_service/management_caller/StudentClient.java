package com.programming.management_service.management_caller;

import com.programming.common_dto.student.StudentRequestDto;
import com.programming.common_dto.student.StudentResponseDto;
import com.programming.management_service.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@FeignClient(
        name = "user-service",
        url = "${user.service.url}",
        contextId = "studentClient"
)
public interface StudentClient {

    @PostMapping("/api/students/create")
    StudentResponseDto createStudent(@RequestBody StudentRequestDto studentRequestDto);

    @GetMapping("/api/students/{id}")
    ApiResponse getStudentById(@PathVariable("id") Long id);

    @GetMapping("/users")
    List<StudentResponseDto> getStudentsByIds(@RequestParam("ids") Set<Long> ids);

    @PutMapping("/api/students/{studentId}/classroom")
    void updateStudentClassroom(@PathVariable("studentId") Long studentId,
                                @RequestParam("classroomId") Long classroomId);
}


