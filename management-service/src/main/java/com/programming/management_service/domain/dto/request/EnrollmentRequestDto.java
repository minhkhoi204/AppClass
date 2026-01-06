package com.programming.management_service.domain.dto.request;

import com.programming.management_service.domain.model.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentRequestDto {
    
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    @NotNull(message = "Classroom ID is required")
    private Long classroomId;
    
    private String studentCode; // If null, will be auto-generated
    
    @NotNull(message = "Academic year is required")
    private String academicYear;
    
    private LocalDate enrollmentDate;
    
    private EnrollmentStatus status; // If null, defaults to ACTIVE
    
    private String note;
}
