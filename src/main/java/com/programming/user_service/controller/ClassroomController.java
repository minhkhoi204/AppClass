package com.programming.user_service.controller;


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

    // Tạo lớp
    @PostMapping()
    public ResponseEntity<ApiResponse> createClassroom(@RequestBody Classroom classroom) {
        Classroom saved = classroomService.createClassroom(classroom);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Classroom created successfully", saved));
    }

    // Xem chi tiết lớp
    @GetMapping("/{classroomId}")
    public ResponseEntity<ApiResponse> getClassroom(@PathVariable Long classroomId) {
        var classroom = classroomService.getClassroomById(classroomId);
        return ResponseEntity.ok(new ApiResponse("Success", classroom));
    }

    // Xem tất cả lớp
    @GetMapping
    public ResponseEntity<ApiResponse> getAllClassrooms() {
        List<Classroom> classrooms = classroomService.getAllClassrooms();
        return ResponseEntity.ok(new ApiResponse("Success", classrooms));
    }

    // Cập nhật lớp
    @PutMapping("/{classroomId}/update")
    public ResponseEntity<ApiResponse> updateClassroom(@PathVariable Long classroomId,
                                                       @RequestBody Classroom classroomDetails) {
        Classroom updated = classroomService.updateClassroom(classroomId, classroomDetails);
        return ResponseEntity.ok(new ApiResponse("Classroom updated successfully", updated));
    }

    // Xóa lớp
    @DeleteMapping("/{classroomId}/delete")
    public ResponseEntity<ApiResponse> deleteClassroom(@PathVariable Long classroomId) {
        classroomService.deleteClassroom(classroomId);
        return ResponseEntity.ok(new ApiResponse("Classroom deleted successfully", null));
    }
}
