package com.programming.management_service.domain.dto.request;

import com.programming.management_service.domain.model.EnrollmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentRequestDto {
    
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    @NotNull(message = "Classroom ID is required")
    private Long classroomId;
    
    //optional
    private String studentCode;
    
    @NotBlank(message = "Academic year is required")
    private String academicYear;
    
    private EnrollmentStatus status;
    
    private LocalDate enrollmentDate;
    
    private LocalDate completionDate;
    
    private String note;
}
