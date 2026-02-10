package com.programming.management_service.domain.dto.response.transition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalizeAllTransitionsResponseDto {

    private String message;

    private Integer totalClassrooms;

    private Integer successfulClassrooms;

    private Integer failedClassrooms;

    private Integer totalStudents;

    private Integer successfulStudents;

    private Integer failedStudents;

    private List<ClassroomResult> classroomResults;

    private List<String> globalErrors;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClassroomResult {
        private Long classroomId;
        private String classroomName;
        private Boolean success;
        private Integer studentsProcessed;
        private Integer studentsSuccess;
        private Integer studentsFailed;
        private String errorMessage;
        private List<TransitionResultResponseDto.NewEnrollmentInfo> newEnrollments;
    }
}
