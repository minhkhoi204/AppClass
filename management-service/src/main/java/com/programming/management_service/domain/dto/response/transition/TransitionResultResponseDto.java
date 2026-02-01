package com.programming.management_service.domain.dto.response.transition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransitionResultResponseDto {
    
    private String message;
    private Integer totalProcessed;
    private Integer successCount;
    private Integer failedCount;
    
    private List<NewEnrollmentInfo> newEnrollments;
    
    private List<String> errors;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewEnrollmentInfo {
        private Long studentId;
        private String studentName;
        private String oldCode;
        private String newCode;
        private Long newEnrollmentId;
        private String newClassroom;
        private String academicYear;
    }
}
