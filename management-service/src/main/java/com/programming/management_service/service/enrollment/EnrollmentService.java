package com.programming.management_service.service.enrollment;

import com.programming.management_service.domain.dto.request.EnrollmentRequestDto;
import com.programming.management_service.domain.dto.response.EnrollmentResponseDto;
import com.programming.management_service.domain.model.EnrollmentStatus;

import java.util.List;

public interface EnrollmentService {
    
    EnrollmentResponseDto createEnrollment(EnrollmentRequestDto dto);
    
    EnrollmentResponseDto getEnrollmentById(Long id);
    
    List<EnrollmentResponseDto> getEnrollmentsByStudent(Long studentId);
    
    List<EnrollmentResponseDto> getEnrollmentsByClassroom(Long classroomId);
    
    EnrollmentResponseDto getActiveEnrollment(Long studentId, Long classroomId);
    
    EnrollmentResponseDto updateEnrollment(Long id, EnrollmentRequestDto dto);
    
    void completeEnrollment(Long id);
    
    void deleteEnrollment(Long id);
    
    List<EnrollmentResponseDto> bulkCompleteEnrollments(Long classroomId, String academicYear);
}
