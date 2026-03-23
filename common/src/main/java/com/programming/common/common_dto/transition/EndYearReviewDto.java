package com.programming.common.common_dto.transition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndYearReviewDto {
    private Long classroomId;
    private String classroomName;
    private String academicYear;
    private Integer totalStudents;
    private Integer completedCount;
    private Integer retainedCount;
    private Integer droppedCount;
    private List<StudentEndYearStatusDto> students;
}
