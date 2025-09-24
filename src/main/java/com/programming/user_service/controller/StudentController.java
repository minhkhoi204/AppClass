package com.programming.user_service.controller;

import com.programming.user_service.dto.StudentDto;
import com.programming.user_service.exceptions.ResourceNotFoundException;
import com.programming.user_service.model.Student;
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
    public ResponseEntity<ApiResponse> createStudent(@RequestBody StudentDto dto) {
        Student saved = studentService.createStudent(dto);
        StudentDto response = studentService.convertToDto(saved);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Student created successfully", response));
    }

    @PostMapping("/add-existing/{studentId}/classroom/{classroomId}")
    public ResponseEntity<ApiResponse> addExistingStudentToClassroom(
            @PathVariable Long classroomId,
            @PathVariable Long studentId) {
        studentService.addExistingStudentToClassroom(classroomId, studentId);
        return ResponseEntity.ok(new ApiResponse("Student added to classroom", null));
    }

    // Thêm học sinh vào lớp (giống addItemToCart)
    @PostMapping("/add/classroom/{classroomId}")
    public ResponseEntity<ApiResponse> createStudentInClassroom(
            @PathVariable Long classroomId,
            @RequestBody StudentDto dto) {
        try {
            studentService.createStudentInClassroom(classroomId, dto);
            return ResponseEntity.ok(new ApiResponse("Student added to classroom", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    // Xóa học sinh khỏi lớp (giống removeItemFromCart)
    @DeleteMapping("/student/{studentId}/classroom/{classroomId}/remove")
    public ResponseEntity<ApiResponse> removeStudentFromClassroom(
            @PathVariable Long classroomId,
            @PathVariable Long studentId) {
        try {
            studentService.removeStudentFromClassroom(classroomId, studentId);
            return ResponseEntity.ok(new ApiResponse("Student removed from classroom", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    // Cập nhật thông tin học sinh trong lớp (giống updateItemQuantity)
    @PutMapping("/student/{studentId}/classroom/{classroomId}/update")
    public ResponseEntity<ApiResponse> updateStudentInClassroom(
            @PathVariable Long classroomId,
            @PathVariable Long studentId,
            @RequestBody StudentDto updatedStudent) {
        try {
            StudentDto updatedDto = studentService.updateStudentInClassroom(classroomId, studentId, updatedStudent);
            return ResponseEntity.ok(new ApiResponse("Student updated successfully", updatedDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    // Xem danh sách học sinh trong lớp
    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<ApiResponse> getStudentsInClassroom(@PathVariable Long classroomId) {
        try {
            List<Student> students = studentService.getStudentsInClassroom(classroomId);
            List<StudentDto> dtos = students.stream()
                    .map(studentService::convertToDto)
                    .toList();
            return ResponseEntity.ok(new ApiResponse("List of students in classroom", dtos));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
