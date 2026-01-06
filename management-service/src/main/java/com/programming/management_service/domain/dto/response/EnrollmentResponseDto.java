package com.programming.management_service.domain.dto.response;

import com.programming.management_service.domain.model.EnrollmentStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentResponseDto {
    
    private Long id;
    
    private Long studentId;
    
    private Long classroomId;
    
    private String studentCode;
    
    private String academicYear;
    
    private LocalDate enrollmentDate;
    
    private EnrollmentStatus status;
    
    private String note;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    //optional enrichment fields
    private String studentName;
    
    private String classroomName;
}
