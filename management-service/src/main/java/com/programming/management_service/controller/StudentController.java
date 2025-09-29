package com.programming.management_service.controller;

import com.programming.management_service.dto.StudentDto;
import com.programming.management_service.model.Student;
import com.programming.management_service.response.ApiResponse;
import com.programming.management_service.service.student.StudentService;
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

    // add student to class
    @PostMapping("/add/classroom/{classroomId}")
    public ResponseEntity<ApiResponse> createStudentInClassroom(
            @PathVariable Long classroomId,
            @RequestBody StudentDto dto) {

        StudentDto created = studentService.createStudentInClassroom(classroomId, dto);
        return ResponseEntity.ok(new ApiResponse("Student added to classroom", created));
    }

    // remove student from class
    @DeleteMapping("/student/{studentId}/classroom/{classroomId}/remove")
    public ResponseEntity<ApiResponse> removeStudentFromClassroom(
            @PathVariable Long classroomId,
            @PathVariable Long studentId) {

        studentService.removeStudentFromClassroom(classroomId, studentId);
        return ResponseEntity.ok(new ApiResponse("Student removed from classroom", null));
    }

    // update student's info
    @PutMapping("/student/{studentId}/classroom/{classroomId}/update")
    public ResponseEntity<ApiResponse> updateStudentInClassroom(
            @PathVariable Long classroomId,
            @PathVariable Long studentId,
            @RequestBody StudentDto updatedStudent) {

        StudentDto updatedDto = studentService.updateStudentInClassroom(classroomId, studentId, updatedStudent);
        return ResponseEntity.ok(new ApiResponse("Student updated successfully", updatedDto));
    }

    // list of students in class
    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<ApiResponse> getStudentsInClassroom(@PathVariable Long classroomId) {
        List<Student> students = studentService.getStudentsInClassroom(classroomId);
        List<StudentDto> dtos = students.stream()
                .map(studentService::convertToDto)
                .toList();
        return ResponseEntity.ok(new ApiResponse("List of students in classroom", dtos));
    }
}
