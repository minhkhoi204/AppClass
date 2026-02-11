package com.programming.management_service.controller;

import com.programming.common.common_dto.transition.EndYearReviewDto;
import com.programming.common.response.ApiResponse;
import com.programming.management_service.domain.dto.request.transition.FinalizeAllTransitionsRequest;
import com.programming.management_service.domain.dto.request.transition.FinalizeTransitionRequest;
import com.programming.management_service.domain.dto.request.transition.GenerateTransitionPlanRequest;
import com.programming.management_service.domain.dto.request.transition.UpdateTransitionStagingRequest;
import com.programming.management_service.domain.dto.response.transition.FinalizeAllTransitionsResponseDto;
import com.programming.management_service.domain.dto.response.transition.TransitionPlanResponseDto;
import com.programming.management_service.domain.dto.response.transition.TransitionResultResponseDto;
import com.programming.management_service.domain.dto.response.transition.TransitionStagingResponseDto;
import com.programming.management_service.service.transition.TransitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/transitions")
@RequiredArgsConstructor
public class TransitionController {
    
    private final TransitionService transitionService;

    @GetMapping("/classrooms/{classroomId}/end-year-review")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY')")
    public ResponseEntity<ApiResponse> getEndYearReview(
            @PathVariable Long classroomId,
            @RequestParam String academicYear) {
        
        EndYearReviewDto response = transitionService.getEndYearReview(classroomId, academicYear);
        return ResponseEntity.ok(new ApiResponse(
                "End-of-year review retrieved successfully", response));
    }

    @PostMapping("/generate-plan")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY')")
    public ResponseEntity<ApiResponse> generateTransitionPlan(
            @Valid @RequestBody GenerateTransitionPlanRequest request) {
        
        TransitionPlanResponseDto response = transitionService.generateTransitionPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Transition plan generated successfully", response));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY')")
    public ResponseEntity<ApiResponse> getPendingTransitions(
            @RequestParam Long classroomId,
            @RequestParam String academicYear) {
        
        List<TransitionStagingResponseDto> response = transitionService
                .getPendingTransitions(classroomId, academicYear);
        
        return ResponseEntity.ok(new ApiResponse(
                String.format("Found %d pending transitions", response.size()), response));
    }

    @PutMapping("/staging/{stagingId}")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY')")
    public ResponseEntity<ApiResponse> updateTransitionStaging(
            @PathVariable Long stagingId,
            @Valid @RequestBody UpdateTransitionStagingRequest request) {
        
        TransitionStagingResponseDto response = transitionService
                .updateTransitionStaging(stagingId, request);
        
        return ResponseEntity.ok(new ApiResponse(
                "Transition staging updated successfully", response));
    }

    @PostMapping("/finalize")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> finalizeTransition(
            @Valid @RequestBody FinalizeTransitionRequest request) {
        
        TransitionResultResponseDto response = transitionService.finalizeTransition(request);
        
        return ResponseEntity.ok(new ApiResponse(
                "Transition finalized successfully", response));
    }
    
    @PostMapping("/finalize-all")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> finalizeAllTransitions(
            @Valid @RequestBody FinalizeAllTransitionsRequest request) {
        
        FinalizeAllTransitionsResponseDto response = transitionService.finalizeAllTransitions(request);
        
        return ResponseEntity.ok(new ApiResponse(
                "Batch transition finalized successfully", response));
    }

    @DeleteMapping("/staging/{stagingId}")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY')")
    public ResponseEntity<ApiResponse> deleteTransitionStaging(@PathVariable Long stagingId) {
        transitionService.deleteTransitionStaging(stagingId);
        return ResponseEntity.ok(new ApiResponse(
                "Transition staging deleted successfully", null));
    }

    @DeleteMapping("/pending")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> deletePendingTransitions(
            @RequestParam Long classroomId,
            @RequestParam String academicYear) {
        
        int deletedCount = transitionService.deletePendingTransitions(classroomId, academicYear);
        
        return ResponseEntity.ok(new ApiResponse(
                String.format("Deleted %d pending transitions", deletedCount), deletedCount));
    }

    @GetMapping("/students/{studentId}/history")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY', 'HUYNH_TRUONG')")
    public ResponseEntity<ApiResponse> getStudentTransitionHistory(@PathVariable Long studentId) {
        List<TransitionStagingResponseDto> response = transitionService
                .getStudentTransitionHistory(studentId);
        
        return ResponseEntity.ok(new ApiResponse(
                String.format("Found %d transition records for student %d", response.size(), studentId), 
                response));
    }

    @GetMapping("/incoming")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY')")
    public ResponseEntity<ApiResponse> getIncomingTransitions(
            @RequestParam Long classroomId,
            @RequestParam String academicYear) {
        
        List<TransitionStagingResponseDto> response = transitionService
                .getIncomingTransitions(classroomId, academicYear);
        
        return ResponseEntity.ok(new ApiResponse(
                String.format("Found %d incoming students to classroom %d", response.size(), classroomId), 
                response));
    }
}
