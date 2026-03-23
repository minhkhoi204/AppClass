package com.programming.management_service.domain.dto.response.transition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransitionStagingResponseDto {
    
    private Long id;
    
    // old enrollment info
    private Long studentId;
    private String studentName; //user-service
    private Long oldClassroomId;
    private String oldClassroomName;
    private String oldAcademicYear;
    private String oldStudentCode;
    private String oldStatus;
    
    // new enrollment info
    private Long newClassroomId;
    private String newClassroomName;
    private String newAcademicYear;
    private String newStudentCode;
    private Long newEnrollmentId;
    
    // metadata
    private String note;
    private Boolean isFinalized;
    private LocalDateTime createdAt;
    private LocalDateTime finalizedAt;
}
