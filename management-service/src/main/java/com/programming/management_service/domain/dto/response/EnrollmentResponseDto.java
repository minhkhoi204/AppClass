package com.programming.management_service.domain.dto.response;

import com.programming.management_service.domain.model.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentResponseDto {
    
    private Long id;
    private Long studentId;
    private Long classroomId;
    private String studentCode;
    private String academicYear;
    private EnrollmentStatus status;
    private LocalDate enrollmentDate;
    private LocalDate completionDate;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
