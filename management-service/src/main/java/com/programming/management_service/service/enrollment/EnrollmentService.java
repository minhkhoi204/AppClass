package com.programming.management_service.service.enrollment;

import com.programming.management_service.domain.dto.request.EnrollmentRequestDto;
import com.programming.management_service.domain.dto.response.EnrollmentResponseDto;
import com.programming.management_service.domain.model.EnrollmentStatus;

import java.util.List;

public interface EnrollmentService {
    
    EnrollmentResponseDto createEnrollment(EnrollmentRequestDto request);
    
    EnrollmentResponseDto getEnrollmentById(Long id);
    
    List<EnrollmentResponseDto> getAllEnrollments();
    
    List<EnrollmentResponseDto> getEnrollmentsByStudentId(Long studentId);
    
    List<EnrollmentResponseDto> getEnrollmentsByClassroomId(Long classroomId);
    
    List<EnrollmentResponseDto> getEnrollmentsByClassroomIdAndYear(Long classroomId, String academicYear);
    
    EnrollmentResponseDto updateEnrollment(Long id, EnrollmentRequestDto request);
    
    EnrollmentResponseDto updateEnrollmentStatus(Long id, EnrollmentStatus status);
    
    void deleteEnrollment(Long id);
    

    EnrollmentResponseDto getEnrollmentByStudentCode(String studentCode);
    
    EnrollmentResponseDto getEnrollmentByStudentCodeAndYear(String studentCode, String academicYear);
}
