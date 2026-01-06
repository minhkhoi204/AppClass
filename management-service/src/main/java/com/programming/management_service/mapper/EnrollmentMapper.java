package com.programming.management_service.mapper;

import com.programming.management_service.domain.dto.request.EnrollmentRequestDto;
import com.programming.management_service.domain.dto.response.EnrollmentResponseDto;
import com.programming.management_service.domain.model.Enrollment;
import com.programming.management_service.domain.model.EnrollmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class EnrollmentMapper {
    
    public Enrollment toEnrollmentEntity(EnrollmentRequestDto dto) {
        return Enrollment.builder()
                .studentId(dto.getStudentId())
                .classroomId(dto.getClassroomId())
                .studentCode(dto.getStudentCode())
                .academicYear(dto.getAcademicYear())
                .enrollmentDate(dto.getEnrollmentDate() != null ? dto.getEnrollmentDate() : LocalDate.now())
                .status(dto.getStatus() != null ? dto.getStatus() : EnrollmentStatus.ACTIVE)
                .note(dto.getNote())
                .build();
    }
    
    public EnrollmentResponseDto toEnrollmentResponseDto(Enrollment enrollment) {
        return EnrollmentResponseDto.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudentId())
                .classroomId(enrollment.getClassroomId())
                .studentCode(enrollment.getStudentCode())
                .academicYear(enrollment.getAcademicYear())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .status(enrollment.getStatus())
                .note(enrollment.getNote())
                .createdAt(enrollment.getCreatedAt())
                .updatedAt(enrollment.getUpdatedAt())
                .build();
    }
    
    public void updateEnrollmentFromDto(Enrollment enrollment, EnrollmentRequestDto dto) {
        if (dto.getStatus() != null) {
            enrollment.setStatus(dto.getStatus());
        }
        if (dto.getNote() != null) {
            enrollment.setNote(dto.getNote());
        }
    }
}
