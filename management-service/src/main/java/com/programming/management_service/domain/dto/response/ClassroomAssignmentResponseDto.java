package com.programming.management_service.domain.dto.response;

import com.programming.management_service.domain.model.AssignmentStatus;
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
public class ClassroomAssignmentResponseDto {
    
    private Long id;
    private Long catechistId;
    private Long classroomId;
    private String academicYear;
    private String role;
    private AssignmentStatus status;
    private LocalDate assignedDate;
    private LocalDate completionDate;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
