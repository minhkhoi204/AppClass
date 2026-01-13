package com.programming.common.common_dto.transition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransitionStagingDto {
    private Long id;
    private Long studentId;
    private Long oldClassroomId;
    private String oldAcademicYear;
    private String oldStudentCode;
    private String oldStatus;
    private Long newClassroomId;
    private String newAcademicYear;
    private String newStudentCode;
    private String note;
    private Boolean isFinalized;
    private LocalDateTime assignedAt;
    private LocalDateTime finalizedAt;
}
