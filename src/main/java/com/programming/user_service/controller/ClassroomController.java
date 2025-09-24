package com.programming.user_service.controller;


import com.programming.user_service.dto.ClassroomDto;
import com.programming.user_service.mapper.ClassroomMapper;
import com.programming.user_service.response.ApiResponse;
import com.programming.user_service.exceptions.ResourceNotFoundException;
import com.programming.user_service.model.Classroom;
import com.programming.user_service.service.classroom.ClassroomService;
import com.programming.user_service.service.student.StudentService;
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

    // Tạo lớp
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createClassroom(@RequestBody ClassroomDto classroomDto) {
        Classroom classroom = classroomMapper.toEntity(classroomDto);
        Classroom saved = classroomService.createClassroom(classroom);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Classroom created successfully", classroomMapper.toClassroomDto(saved)));
    }

    // Xem chi tiết lớp
    @GetMapping("/{classroomId}")
    public ResponseEntity<ApiResponse> getClassroom(@PathVariable Long classroomId) {
        Classroom classroom = classroomService.getClassroomById(classroomId);
        return ResponseEntity.ok(new ApiResponse("Success", classroomMapper.toClassroomDto(classroom)));
    }

    // Xem tất cả lớp
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllClassrooms() {
        List<Classroom> classrooms = classroomService.getAllClassrooms();
        List<ClassroomDto> dtos = classrooms.stream()
                .map(classroomMapper::toClassroomDto)
                .toList();
        return ResponseEntity.ok(new ApiResponse("Success", dtos));
    }

    // Cập nhật lớp
    @PutMapping("/{classroomId}/update")
    public ResponseEntity<ApiResponse> updateClassroom(@PathVariable Long classroomId,
                                                       @RequestBody ClassroomDto classroomDto) {
        Classroom updated = classroomService.updateClassroom(classroomId, classroomMapper.toEntity(classroomDto));
        return ResponseEntity.ok(new ApiResponse("Classroom updated successfully", classroomMapper.toClassroomDto(updated)));
    }

    // Xóa lớp
    @DeleteMapping("/{classroomId}/delete")
    public ResponseEntity<ApiResponse> deleteClassroom(@PathVariable Long classroomId) {
        classroomService.deleteClassroom(classroomId);
        return ResponseEntity.ok(new ApiResponse("Classroom deleted successfully", null));
    }
}
