package com.programming.management_service.service.assignment;

import com.programming.common.exception.AlreadyExistsException;
import com.programming.common.exception.ResourceNotFoundException;
import com.programming.management_service.domain.dto.request.ClassroomAssignmentRequestDto;
import com.programming.management_service.domain.dto.response.ClassroomAssignmentResponseDto;
import com.programming.management_service.domain.model.AssignmentStatus;
import com.programming.management_service.domain.model.ClassroomAssignment;
import com.programming.management_service.repository.ClassroomAssignmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassroomAssignmentServiceImpl implements ClassroomAssignmentService {
    
    private final ClassroomAssignmentRepository assignmentRepository;
    
    @Override
    @Transactional
    public ClassroomAssignmentResponseDto createAssignment(ClassroomAssignmentRequestDto request) {
        // Check if assignment already exists
        if (assignmentRepository.existsByCatechistIdAndClassroomIdAndAcademicYear(
                request.getCatechistId(), 
                request.getClassroomId(), 
                request.getAcademicYear())) {
            throw new AlreadyExistsException(
                "Assignment already exists for this catechist in this classroom and academic year");
        }
        
        ClassroomAssignment assignment = ClassroomAssignment.builder()
                .catechistId(request.getCatechistId())
                .classroomId(request.getClassroomId())
                .academicYear(request.getAcademicYear())
                .role(request.getRole())
                .status(request.getStatus() != null ? request.getStatus() : AssignmentStatus.ACTIVE)
                .assignedDate(request.getAssignedDate() != null ? request.getAssignedDate() : LocalDate.now())
                .completionDate(request.getCompletionDate())
                .note(request.getNote())
                .build();
        
        ClassroomAssignment savedAssignment = assignmentRepository.save(assignment);
        log.info("Created assignment: catechistId={}, classroomId={}, year={}", 
                request.getCatechistId(), request.getClassroomId(), request.getAcademicYear());
        
        return mapToResponseDto(savedAssignment);
    }
    
    @Override
    public ClassroomAssignmentResponseDto getAssignmentById(Long id) {
        ClassroomAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
        return mapToResponseDto(assignment);
    }
    
    @Override
    public List<ClassroomAssignmentResponseDto> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ClassroomAssignmentResponseDto> getAssignmentsByCatechistId(Long catechistId) {
        return assignmentRepository.findByCatechistId(catechistId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ClassroomAssignmentResponseDto> getAssignmentsByClassroomId(Long classroomId) {
        return assignmentRepository.findByClassroomId(classroomId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ClassroomAssignmentResponseDto> getAssignmentsByClassroomIdAndYear(
            Long classroomId, String academicYear) {
        return assignmentRepository.findByClassroomIdAndAcademicYear(classroomId, academicYear).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ClassroomAssignmentResponseDto> getActiveCatechistsInClassroom(
            Long classroomId, String academicYear) {
        return assignmentRepository.findByClassroomIdAndAcademicYearAndStatus(
                classroomId, academicYear, AssignmentStatus.ACTIVE).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public ClassroomAssignmentResponseDto updateAssignment(Long id, ClassroomAssignmentRequestDto request) {
        ClassroomAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
        
        assignment.setRole(request.getRole());
        assignment.setStatus(request.getStatus());
        assignment.setAssignedDate(request.getAssignedDate());
        assignment.setCompletionDate(request.getCompletionDate());
        assignment.setNote(request.getNote());
        
        ClassroomAssignment updatedAssignment = assignmentRepository.save(assignment);
        log.info("Updated assignment id={}", id);
        
        return mapToResponseDto(updatedAssignment);
    }
    
    @Override
    @Transactional
    public ClassroomAssignmentResponseDto updateAssignmentStatus(Long id, AssignmentStatus status) {
        ClassroomAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
        
        assignment.setStatus(status);
        if (status == AssignmentStatus.COMPLETED && assignment.getCompletionDate() == null) {
            assignment.setCompletionDate(LocalDate.now());
        }
        
        ClassroomAssignment updatedAssignment = assignmentRepository.save(assignment);
        log.info("Updated assignment status: id={}, status={}", id, status);
        
        return mapToResponseDto(updatedAssignment);
    }
    
    @Override
    @Transactional
    public void deleteAssignment(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Assignment not found with id: " + id);
        }
        assignmentRepository.deleteById(id);
        log.info("Deleted assignment id={}", id);
    }
    
    private ClassroomAssignmentResponseDto mapToResponseDto(ClassroomAssignment assignment) {
        return ClassroomAssignmentResponseDto.builder()
                .id(assignment.getId())
                .catechistId(assignment.getCatechistId())
                .classroomId(assignment.getClassroomId())
                .academicYear(assignment.getAcademicYear())
                .role(assignment.getRole())
                .status(assignment.getStatus())
                .assignedDate(assignment.getAssignedDate())
                .completionDate(assignment.getCompletionDate())
                .note(assignment.getNote())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .build();
    }
}
