package com.programming.management_service.service.assignment;

import com.programming.management_service.domain.dto.request.ClassroomAssignmentRequestDto;
import com.programming.management_service.domain.dto.response.ClassroomAssignmentResponseDto;
import com.programming.management_service.domain.model.AssignmentStatus;

import java.util.List;

public interface ClassroomAssignmentService {

    ClassroomAssignmentResponseDto createAssignment(ClassroomAssignmentRequestDto request);

    ClassroomAssignmentResponseDto getAssignmentById(Long id);

    List<ClassroomAssignmentResponseDto> getAllAssignments();

    List<ClassroomAssignmentResponseDto> getAssignmentsByCatechistId(Long catechistId);

    List<ClassroomAssignmentResponseDto> getAssignmentsByClassroomId(Long classroomId);

    List<ClassroomAssignmentResponseDto> getAssignmentsByClassroomIdAndYear(Long classroomId, String academicYear);

    List<ClassroomAssignmentResponseDto> getActiveCatechistsInClassroom(Long classroomId, String academicYear);

    ClassroomAssignmentResponseDto updateAssignment(Long id, ClassroomAssignmentRequestDto request);

    ClassroomAssignmentResponseDto updateAssignmentStatus(Long id, AssignmentStatus status);

    void deleteAssignment(Long id);
}
