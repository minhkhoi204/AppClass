package com.programming.management_service.controller;

import com.programming.management_service.domain.dto.ClassroomDto;
import com.programming.management_service.domain.dto.request.ClassroomRequestDto;
import com.programming.management_service.domain.dto.response.ClassroomResponseDto;
import com.programming.management_service.mapper.ClassroomMapper;
import com.programming.management_service.response.ApiResponse;
import com.programming.management_service.domain.model.Classroom;
import com.programming.management_service.service.classroom.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/classrooms")

public class ClassroomController {

    private final ClassroomService classroomService;
    private final ClassroomMapper classroomMapper;

    // Create class
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createClassroom(@RequestBody ClassroomRequestDto classroomRequestDto) {
        Classroom classroom = classroomMapper.toClassroomEntity(classroomRequestDto);
        Classroom saved = classroomService.createClassroom(classroom);
        ClassroomResponseDto responseDto = classroomMapper.toClassroomResponseDto(saved);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Classroom created successfully", responseDto));
    }

    @PostMapping("/{classroomId}/add-student/{studentId}")
    public ResponseEntity<ApiResponse> addStudentToClassroom(@PathVariable Long classroomId,
                                                             @PathVariable Long studentId) {
        ClassroomResponseDto updated = classroomService.addStudentToClassroom(classroomId, studentId);
        return ResponseEntity.ok(new ApiResponse("Student added to classroom", updated));
    }

    @GetMapping("/{classroomId}")
    public ResponseEntity<ApiResponse> getClassroomById(@PathVariable Long classroomId) {
        ClassroomResponseDto classroom = classroomService.getClassroomById(classroomId);
        return ResponseEntity.ok(new ApiResponse("Success", classroom));
    }
}
