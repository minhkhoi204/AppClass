package com.programming.management_service.domain.dto.request.transition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalizeAllTransitionsRequest {

    private String fromAcademicYear;

    private String toAcademicYear;

    private List<Long> classroomIds;

    @Builder.Default
    private Boolean autoGenerateStudentCodes = true;
}
