package com.programming.common.common_dto.enrollment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentStatusUpdateDto {
    private Long enrollmentId;
    private String newStatus;
    private String note;
}
