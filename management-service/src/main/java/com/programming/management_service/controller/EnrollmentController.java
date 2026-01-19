package com.programming.management_service.controller;

import com.programming.common.response.ApiResponse;
import com.programming.management_service.domain.dto.request.EnrollmentRequestDto;
import com.programming.management_service.domain.dto.response.EnrollmentResponseDto;
import com.programming.management_service.domain.model.EnrollmentStatus;
import com.programming.management_service.service.enrollment.EnrollmentService;
import com.programming.management_service.service.code.BatchStudentCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;
    private final BatchStudentCodeService batchStudentCodeService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY')")
    public ResponseEntity<ApiResponse> createEnrollment(@Valid @RequestBody EnrollmentRequestDto request) {
        EnrollmentResponseDto response = enrollmentService.createEnrollment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Enrollment created successfully", response));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY', 'HUYNH_TRUONG')")
    public ResponseEntity<ApiResponse> getEnrollmentById(@PathVariable Long id) {
        EnrollmentResponseDto response = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.ok(new ApiResponse("Enrollment retrieved successfully", response));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY')")
    public ResponseEntity<ApiResponse> getAllEnrollments() {
        List<EnrollmentResponseDto> response = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(new ApiResponse("Enrollments retrieved successfully", response));
    }
    
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY', 'HUYNH_TRUONG')")
    public ResponseEntity<ApiResponse> getEnrollmentsByStudentId(@PathVariable Long studentId) {
        List<EnrollmentResponseDto> response = enrollmentService.getEnrollmentsByStudentId(studentId);
        return ResponseEntity.ok(new ApiResponse("Student enrollments retrieved successfully", response));
    }
    
    @GetMapping("/classroom/{classroomId}")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY', 'HUYNH_TRUONG')")
    public ResponseEntity<ApiResponse> getEnrollmentsByClassroomId(@PathVariable Long classroomId) {
        List<EnrollmentResponseDto> response = enrollmentService.getEnrollmentsByClassroomId(classroomId);
        return ResponseEntity.ok(new ApiResponse("Classroom enrollments retrieved successfully", response));
    }
    
    @GetMapping("/classroom/{classroomId}/year/{academicYear}")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY', 'HUYNH_TRUONG')")
    public ResponseEntity<ApiResponse> getEnrollmentsByClassroomIdAndYear(
            @PathVariable Long classroomId, 
            @PathVariable String academicYear) {
        List<EnrollmentResponseDto> response = enrollmentService.getEnrollmentsByClassroomIdAndYear(classroomId, academicYear);
        return ResponseEntity.ok(new ApiResponse("Classroom enrollments for year retrieved successfully", response));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY')")
    public ResponseEntity<ApiResponse> updateEnrollment(
            @PathVariable Long id,
            @Valid @RequestBody EnrollmentRequestDto request) {
        EnrollmentResponseDto response = enrollmentService.updateEnrollment(id, request);
        return ResponseEntity.ok(new ApiResponse("Enrollment updated successfully", response));
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY')")
    public ResponseEntity<ApiResponse> updateEnrollmentStatus(
            @PathVariable Long id,
            @RequestParam EnrollmentStatus status) {
        EnrollmentResponseDto response = enrollmentService.updateEnrollmentStatus(id, status);
        return ResponseEntity.ok(new ApiResponse("Enrollment status updated successfully", response));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.ok(new ApiResponse("Enrollment deleted successfully", null));
    }
    
    @GetMapping("/code/{studentCode}")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY', 'HUYNH_TRUONG')")
    public ResponseEntity<ApiResponse> getEnrollmentByStudentCode(@PathVariable String studentCode) {
        EnrollmentResponseDto response = enrollmentService.getEnrollmentByStudentCode(studentCode);
        return ResponseEntity.ok(new ApiResponse("Enrollment retrieved by student code successfully", response));
    }
    
    @GetMapping("/code/{studentCode}/year/{academicYear}")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY', 'HUYNH_TRUONG')")
    public ResponseEntity<ApiResponse> getEnrollmentByStudentCodeAndYear(
            @PathVariable String studentCode,
            @PathVariable String academicYear) {
        EnrollmentResponseDto response = enrollmentService.getEnrollmentByStudentCodeAndYear(studentCode, academicYear);
        return ResponseEntity.ok(new ApiResponse("Enrollment retrieved successfully", response));
    }
    
    //batch generate student codes
    //alphabetical order by student name
    @PostMapping("/batch-generate-codes/classroom/{classroomId}/year/{academicYear}")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY')")
    public ResponseEntity<ApiResponse> batchGenerateStudentCodes(
            @PathVariable Long classroomId,
            @PathVariable String academicYear,
            @RequestParam(defaultValue = "false") boolean forceRegenerate) {
        
        BatchStudentCodeService.BatchGenerateResult result = batchStudentCodeService
                .generateCodesForClassroom(classroomId, academicYear, forceRegenerate);
        
        return ResponseEntity.ok(new ApiResponse(
                String.format("Generated %d student codes for classroom %s", 
                        result.getGeneratedCount(), result.getClassroomName()),
                result));
    }
}
