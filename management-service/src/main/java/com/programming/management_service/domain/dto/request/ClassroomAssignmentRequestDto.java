package com.programming.management_service.domain.dto.request;

import com.programming.management_service.domain.model.AssignmentStatus;
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
public class ClassroomAssignmentRequestDto {
    
    @NotNull(message = "Catechist ID is required")
    private Long catechistId;
    
    @NotNull(message = "Classroom ID is required")
    private Long classroomId;
    
    @NotBlank(message = "Academic year is required")
    private String academicYear;
    
    private String role;
    
    private AssignmentStatus status;
    
    private LocalDate assignedDate;
    
    private LocalDate completionDate;
    
    private String note;
}
