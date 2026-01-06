package com.programming.management_service.service.enrollment;

import com.programming.common.exception.AlreadyExistsException;
import com.programming.common.exception.ResourceNotFoundException;
import com.programming.management_service.domain.dto.request.EnrollmentRequestDto;
import com.programming.management_service.domain.dto.response.EnrollmentResponseDto;
import com.programming.management_service.domain.model.Classroom;
import com.programming.management_service.domain.model.Enrollment;
import com.programming.management_service.domain.model.EnrollmentStatus;
import com.programming.management_service.mapper.EnrollmentMapper;
import com.programming.management_service.repository.ClassroomRepository;
import com.programming.management_service.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final ClassroomRepository classroomRepository;
    private final EnrollmentMapper enrollmentMapper;
    
    @Override
    @Transactional
    public EnrollmentResponseDto createEnrollment(EnrollmentRequestDto dto) {
        // validate classroom exists
        Classroom classroom = classroomRepository.findById(dto.getClassroomId())
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));
        
        // check if student already has active enrollment in this classroom
        enrollmentRepository.findByStudentIdAndClassroomIdAndStatus(
                dto.getStudentId(), dto.getClassroomId(), EnrollmentStatus.ACTIVE)
                .ifPresent(e -> {
                    throw new AlreadyExistsException("Student already has active enrollment in this classroom");
                });
        
        // generate student code if not provided
        if (dto.getStudentCode() == null || dto.getStudentCode().isEmpty()) {
            String studentCode = generateStudentCode(classroom, dto.getAcademicYear());
            dto.setStudentCode(studentCode);
        } else {
            // validate student code uniqueness
            if (enrollmentRepository.existsByClassroomIdAndAcademicYearAndStudentCode(
                    dto.getClassroomId(), dto.getAcademicYear(), dto.getStudentCode())) {
                throw new AlreadyExistsException("Student code already exists in this classroom for academic year");
            }
        }
        
        Enrollment enrollment = enrollmentMapper.toEnrollmentEntity(dto);
        Enrollment saved = enrollmentRepository.save(enrollment);
        
        log.info("Created enrollment: student {} in classroom {} with code {}", 
                dto.getStudentId(), dto.getClassroomId(), saved.getStudentCode());
        
        return enrollmentMapper.toEnrollmentResponseDto(saved);
    }
    
    @Override
    public EnrollmentResponseDto getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        return enrollmentMapper.toEnrollmentResponseDto(enrollment);
    }
    
    @Override
    public List<EnrollmentResponseDto> getEnrollmentsByStudent(Long studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return enrollments.stream()
                .map(enrollmentMapper::toEnrollmentResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<EnrollmentResponseDto> getEnrollmentsByClassroom(Long classroomId) {
        List<Enrollment> enrollments = enrollmentRepository.findByClassroomId(classroomId);
        return enrollments.stream()
                .map(enrollmentMapper::toEnrollmentResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public EnrollmentResponseDto getActiveEnrollment(Long studentId, Long classroomId) {
        Enrollment enrollment = enrollmentRepository
                .findByStudentIdAndClassroomIdAndStatus(studentId, classroomId, EnrollmentStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active enrollment found"));
        return enrollmentMapper.toEnrollmentResponseDto(enrollment);
    }
    
    @Override
    @Transactional
    public EnrollmentResponseDto updateEnrollment(Long id, EnrollmentRequestDto dto) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        
        enrollmentMapper.updateEnrollmentFromDto(enrollment, dto);
        Enrollment updated = enrollmentRepository.save(enrollment);
        
        log.info("Updated enrollment: {}", id);
        return enrollmentMapper.toEnrollmentResponseDto(updated);
    }
    
    @Override
    @Transactional
    public void completeEnrollment(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        
        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        enrollmentRepository.save(enrollment);
        
        log.info("Completed enrollment: {}", id);
    }
    
    @Override
    @Transactional
    public void deleteEnrollment(Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Enrollment not found");
        }
        enrollmentRepository.deleteById(id);
        log.info("Deleted enrollment: {}", id);
    }
    
    @Override
    @Transactional
    public List<EnrollmentResponseDto> bulkCompleteEnrollments(Long classroomId, String academicYear) {
        List<Enrollment> enrollments = enrollmentRepository.findByClassroomIdAndAcademicYear(
                classroomId, academicYear);
        
        enrollments.forEach(e -> e.setStatus(EnrollmentStatus.COMPLETED));
        List<Enrollment> updated = enrollmentRepository.saveAll(enrollments);
        
        log.info("Completed {} enrollments for classroom {} year {}", 
                updated.size(), classroomId, academicYear);
        
        return updated.stream()
                .map(enrollmentMapper::toEnrollmentResponseDto)
                .collect(Collectors.toList());
    }
    

    private String generateStudentCode(Classroom classroom, String academicYear) {
        Integer maxNumber = enrollmentRepository.findMaxStudentNumberInClassroom(
                classroom.getId(), academicYear);
        int nextNumber = (maxNumber == null) ? 1 : maxNumber + 1;
        
        // parse classroom name to code
        String classCode = parseClassroomNameToCode(classroom.getName());
        
        return String.format("%02d%s", nextNumber, classCode);
    }
    
    private String parseClassroomNameToCode(String classroomName) {
        String[] words = classroomName.trim().toLowerCase().split("\\s+");
        
        if (words.length == 0) {
            throw new IllegalArgumentException("Invalid classroom name: " + classroomName);
        }
        
        StringBuilder codeBuilder = new StringBuilder();
        
        for (String word : words) {
            if (word.isEmpty()) continue;
            
            // if a number, append directly
            if (word.matches("\\d+")) {
                codeBuilder.append(word);
            } else {
                // append first character of word
                codeBuilder.append(word.charAt(0));
            }
        }
        
        return codeBuilder.toString();
    }
}
