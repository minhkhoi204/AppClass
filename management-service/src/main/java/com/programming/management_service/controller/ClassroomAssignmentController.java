package com.programming.management_service.controller;

import com.programming.common.response.ApiResponse;
import com.programming.management_service.domain.dto.request.ClassroomAssignmentRequestDto;
import com.programming.management_service.domain.dto.response.ClassroomAssignmentResponseDto;
import com.programming.management_service.domain.model.AssignmentStatus;
import com.programming.management_service.service.assignment.ClassroomAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classroom-assignments")
@RequiredArgsConstructor
public class ClassroomAssignmentController {
    
    private final ClassroomAssignmentService assignmentService;
    
    @PostMapping
    public ResponseEntity<ApiResponse> createAssignment(
            @RequestBody ClassroomAssignmentRequestDto request) {
        ClassroomAssignmentResponseDto response = assignmentService.createAssignment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Assignment created successfully", response));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAssignmentById(
            @PathVariable Long id) {
        ClassroomAssignmentResponseDto response = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(new ApiResponse("Assignment retrieved successfully", response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse> getAllAssignments() {
        List<ClassroomAssignmentResponseDto> assignments = assignmentService.getAllAssignments();
        return ResponseEntity.ok(new ApiResponse("Assignments retrieved successfully", assignments));
    }
    
    @GetMapping("/catechist/{catechistId}")
    public ResponseEntity<ApiResponse> getAssignmentsByCatechistId(
            @PathVariable Long catechistId) {
        List<ClassroomAssignmentResponseDto> assignments = 
                assignmentService.getAssignmentsByCatechistId(catechistId);
        return ResponseEntity.ok(new ApiResponse("Catechist assignments retrieved successfully", assignments));
    }
    
    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<ApiResponse> getAssignmentsByClassroomId(
            @PathVariable Long classroomId,
            @RequestParam(required = false) String academicYear) {
        List<ClassroomAssignmentResponseDto> assignments;
        
        if (academicYear != null) {
            assignments = assignmentService.getAssignmentsByClassroomIdAndYear(classroomId, academicYear);
        } else {
            assignments = assignmentService.getAssignmentsByClassroomId(classroomId);
        }
        
        return ResponseEntity.ok(new ApiResponse("Classroom assignments retrieved successfully", assignments));
    }
    
    @GetMapping("/classroom/{classroomId}/active")
    public ResponseEntity<ApiResponse> getActiveCatechistsInClassroom(
            @PathVariable Long classroomId,
            @RequestParam String academicYear) {
        List<ClassroomAssignmentResponseDto> assignments = 
                assignmentService.getActiveCatechistsInClassroom(classroomId, academicYear);
        return ResponseEntity.ok(new ApiResponse("Active catechists retrieved successfully", assignments));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateAssignment(
            @PathVariable Long id,
            @RequestBody ClassroomAssignmentRequestDto request) {
        ClassroomAssignmentResponseDto response = assignmentService.updateAssignment(id, request);
        return ResponseEntity.ok(new ApiResponse("Assignment updated successfully", response));
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse> updateAssignmentStatus(
            @PathVariable Long id,
            @RequestParam AssignmentStatus status) {
        ClassroomAssignmentResponseDto response = assignmentService.updateAssignmentStatus(id, status);
        return ResponseEntity.ok(new ApiResponse("Assignment status updated successfully", response));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.ok(new ApiResponse("Assignment deleted successfully", null));
    }
}
