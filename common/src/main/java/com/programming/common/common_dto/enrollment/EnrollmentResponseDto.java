package com.programming.common.common_dto.enrollment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponseDto {
    private Long id;
    private Long studentId;
    private Long classroomId;
    private String studentCode;
    private String academicYear;
    private String status;
    private LocalDate enrollmentDate;
    private LocalDate completionDate;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
