package com.programming.common.common_dto.transition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentEndYearStatusDto {
    private Long enrollmentId;
    private Long studentId;
    private String studentName;
    private String studentCode;
    private String currentStatus;
    private String note;
}
