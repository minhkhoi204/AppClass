package com.programming.common.common_dto.enrollment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequestDto {
    private Long studentId;
    private Long classroomId;
    private String academicYear;
    private String note;
}
