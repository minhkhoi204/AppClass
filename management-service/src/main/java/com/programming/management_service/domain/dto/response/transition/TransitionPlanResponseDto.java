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
public class TransitionPlanResponseDto {
    
    private Long classroomId;
    private String classroomName;
    private String fromAcademicYear;
    private String toAcademicYear;
    
    // analytics
    private Integer totalStudents;
    private Integer completedCount; 
    private Integer retainedCount;
    

    private NextClassroomInfo nextClassroom;
    

    private List<TransitionStagingResponseDto> transitions;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NextClassroomInfo {
        private Long id;
        private String name;
        private String academicYear;
    }
}
