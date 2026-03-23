package com.programming.management_service.service.transition;

import com.programming.common.common_dto.transition.EndYearReviewDto;
import com.programming.management_service.domain.dto.request.transition.FinalizeAllTransitionsRequest;
import com.programming.management_service.domain.dto.request.transition.FinalizeTransitionRequest;
import com.programming.management_service.domain.dto.request.transition.GenerateTransitionPlanRequest;
import com.programming.management_service.domain.dto.request.transition.UpdateTransitionStagingRequest;
import com.programming.management_service.domain.dto.response.transition.FinalizeAllTransitionsResponseDto;
import com.programming.management_service.domain.dto.response.transition.TransitionPlanResponseDto;
import com.programming.management_service.domain.dto.response.transition.TransitionResultResponseDto;
import com.programming.management_service.domain.dto.response.transition.TransitionStagingResponseDto;

import java.util.List;

public interface TransitionService {
    
    // review and get summary of COMPLETED / RETAINED / INCOMPLETE counts
    EndYearReviewDto getEndYearReview(Long classroomId, String academicYear);
    
    TransitionPlanResponseDto generateTransitionPlan(GenerateTransitionPlanRequest request);

    List<TransitionStagingResponseDto> getPendingTransitions(Long classroomId, String academicYear);
    
    TransitionStagingResponseDto updateTransitionStaging(Long stagingId, UpdateTransitionStagingRequest request);

    TransitionResultResponseDto finalizeTransition(FinalizeTransitionRequest request);
    
    FinalizeAllTransitionsResponseDto finalizeAllTransitions(FinalizeAllTransitionsRequest request);

    void deleteTransitionStaging(Long stagingId);
    
    List<TransitionStagingResponseDto> getStudentTransitionHistory(Long studentId);

    List<TransitionStagingResponseDto> getIncomingTransitions(Long classroomId, String academicYear);

    int deletePendingTransitions(Long classroomId, String academicYear);
}
