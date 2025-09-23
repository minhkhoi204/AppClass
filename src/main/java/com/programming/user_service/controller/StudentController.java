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

    // Thêm học sinh vào lớp (giống addItemToCart)
    @PostMapping("classroom/{classroomId}/add")
    public ResponseEntity<ApiResponse> addStudentToClassroom(
            @PathVariable Long classroomId,
            @RequestBody Student student) {
        try {
            studentService.addStudentToClassroom(classroomId, student);
            return ResponseEntity.ok(new ApiResponse("Student added to classroom", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    // Xóa học sinh khỏi lớp (giống removeItemFromCart)
    @DeleteMapping("/classroom/{classroomId}/student/{studentId}/remove")
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
    @PutMapping("/classroom/{classroomId}/student//{studentId}/update")
    public ResponseEntity<ApiResponse> updateStudentInClassroom(
            @PathVariable Long classroomId,
            @PathVariable Long studentId,
            @RequestBody Student updatedStudent) {
        try {
            studentService.updateStudentInClassroom(classroomId, studentId, updatedStudent);
            return ResponseEntity.ok(new ApiResponse("Student updated successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    // Xem danh sách học sinh trong lớp
    @GetMapping("/classroom/{classroomId}/students")
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
