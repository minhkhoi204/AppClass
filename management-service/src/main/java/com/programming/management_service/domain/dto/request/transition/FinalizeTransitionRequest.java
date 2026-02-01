package com.programming.management_service.domain.dto.request.transition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalizeTransitionRequest {
    
    @NotNull(message = "Classroom ID is required")
    private Long classroomId;
    
    @NotBlank(message = "From academic year is required")
    private String fromAcademicYear;
    
    @NotBlank(message = "To academic year is required")
    private String toAcademicYear;
    
    // if false, enrollments are created without student codes
    @Builder.Default
    private Boolean autoGenerateStudentCodes = true;
}
