package com.programming.user_service.controller;

import com.programming.common.common_dto.student.StudentRequestDto;
import com.programming.common.common_dto.student.StudentResponseDto;
import com.programming.user_service.mapper.StudentMapper;
import com.programming.common.response.ApiResponse;
import com.programming.user_service.service.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/students")

public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;

//    @PostMapping("/create")
//    public ResponseEntity<StudentResponseDto> createStudent(@RequestBody StudentRequestDto dto) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(dto));
//    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY')")
    public ResponseEntity<ApiResponse> createStudent(@RequestBody StudentRequestDto dto) {
        StudentResponseDto response = studentService.createStudent(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Student created successfully", response));
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable Long id) {
//        StudentResponseDto student = studentService.getStudentById(id);
//        return ResponseEntity.ok(student);
//    }
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getStudentById(@PathVariable Long id) {
        StudentResponseDto response = studentService.getStudentById(id);
        return ResponseEntity.ok(new ApiResponse("Student retrieved successfully", response));
    }
    
    @GetMapping("/batch")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<StudentResponseDto>> getStudentsByIds(@RequestParam("ids") List<Long> ids) {
        List<StudentResponseDto> students = studentService.getStudentsByIds(new java.util.HashSet<>(ids));
        return ResponseEntity.ok(students);
    }

    @PostMapping("/create-with-user")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY')")
    public ResponseEntity<ApiResponse> createStudentWithUserId(
            @RequestBody StudentRequestDto studentRequestDto,
            @RequestParam Long userId) {

        StudentResponseDto response = studentService.createStudentWithUserId(studentRequestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Student created successfully", response));
    }

    @PutMapping("/{id}/update")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY')")
    public ResponseEntity<ApiResponse> updateStudent(@PathVariable Long id, @RequestBody StudentRequestDto dto) {
        StudentResponseDto response = studentService.updateStudent(id, dto);
        return ResponseEntity.ok(new ApiResponse("Student updated successfully", response));
    }

//    @PutMapping("/{studentId}/classroom")
//    public ResponseEntity<ApiResponse> updateClassroom(@PathVariable Long studentId,
//                                                       @RequestParam Long classroomId) {
//        studentService.updateStudentClassroom(studentId, classroomId);
//        return ResponseEntity.ok(new ApiResponse("Student classroom updated", null));
//    }

}
