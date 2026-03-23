package com.programming.management_service.domain.dto.request.transition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransitionStagingRequest {
    
    private Long newClassroomId;

    private String newAcademicYear;

    private String note;
}
