package com.programming.management_service.service.enrollment;

import com.programming.common.exception.AlreadyExistsException;
import com.programming.common.exception.ResourceNotFoundException;
import com.programming.management_service.domain.dto.request.EnrollmentRequestDto;
import com.programming.management_service.domain.dto.response.EnrollmentResponseDto;
import com.programming.management_service.domain.model.Classroom;
import com.programming.management_service.domain.model.Enrollment;
import com.programming.management_service.domain.model.EnrollmentStatus;
import com.programming.management_service.repository.ClassroomRepository;
import com.programming.management_service.repository.EnrollmentRepository;
import com.programming.management_service.service.code.StudentCodeGenerator;
import com.programming.management_service.mapper.EnrollmentMapper;
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
public class EnrollmentServiceImpl implements EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final ClassroomRepository classroomRepository;
    private final StudentCodeGenerator studentCodeGenerator;
    private final EnrollmentMapper enrollmentMapper;
    
    @Override
    @Transactional
    public EnrollmentResponseDto createEnrollment(EnrollmentRequestDto request) {
        // validate classroom exists for the specified academic year
        Classroom classroom = classroomRepository.findById(request.getClassroomId())
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + request.getClassroomId()));
        
        if (!classroom.getAcademicYear().equals(request.getAcademicYear())) {
            throw new IllegalArgumentException(
                String.format("Classroom '%s' (id: %d) belongs to academic year '%s', but enrollment is for '%s'", 
                    classroom.getName(), classroom.getId(), classroom.getAcademicYear(), request.getAcademicYear()));
        }
        
        // if enrollment already exists
        if (enrollmentRepository.existsByStudentIdAndClassroomIdAndAcademicYear(
                request.getStudentId(), request.getClassroomId(), request.getAcademicYear())) {
            throw new AlreadyExistsException("Enrollment already exists for this student in this classroom and academic year");
        }
        
        // optional generated later in batch
        String studentCode = request.getStudentCode();
        if (studentCode != null && !studentCode.trim().isEmpty()) {
            if (!studentCodeGenerator.isValidStudentCodeFormat(studentCode)) {
                throw new IllegalArgumentException("Invalid student code format: " + studentCode);
            }
            
            // if student code already exists for this year
            if (enrollmentRepository.existsByStudentCodeAndAcademicYear(studentCode, request.getAcademicYear())) {
                throw new AlreadyExistsException("Student code already exists: " + studentCode + " for year: " + request.getAcademicYear());
            }
        }
        
        Enrollment enrollment = Enrollment.builder()
                .studentId(request.getStudentId())
                .classroomId(request.getClassroomId())
                .studentCode(studentCode)
                .academicYear(request.getAcademicYear())
                .status(request.getStatus() != null ? request.getStatus() : EnrollmentStatus.ACTIVE)
                .enrollmentDate(request.getEnrollmentDate() != null ? request.getEnrollmentDate() : LocalDate.now())
                .completionDate(request.getCompletionDate())
                .note(request.getNote())
                .build();
        
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        log.info("Created enrollment for studentId: {}, classroomId: {} without student code. Will be generated in batch.", 
                request.getStudentId(), request.getClassroomId());
        return enrollmentMapper.toResponseDto(savedEnrollment);
    }
    
    @Override
    public EnrollmentResponseDto getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
        return enrollmentMapper.toResponseDto(enrollment);
    }
    
    @Override
    public List<EnrollmentResponseDto> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .map(enrollmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<EnrollmentResponseDto> getEnrollmentsByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(enrollmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<EnrollmentResponseDto> getEnrollmentsByClassroomId(Long classroomId) {
        return enrollmentRepository.findByClassroomId(classroomId).stream()
                .map(enrollmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<EnrollmentResponseDto> getEnrollmentsByClassroomIdAndYear(Long classroomId, String academicYear) {
        return enrollmentRepository.findByClassroomIdAndAcademicYear(classroomId, academicYear).stream()
                .map(enrollmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public EnrollmentResponseDto updateEnrollment(Long id, EnrollmentRequestDto request) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
        
        enrollment.setStudentCode(request.getStudentCode());
        enrollment.setStatus(request.getStatus());
        enrollment.setEnrollmentDate(request.getEnrollmentDate());
        enrollment.setCompletionDate(request.getCompletionDate());
        enrollment.setNote(request.getNote());
        
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponseDto(updatedEnrollment);
    }
    
    @Override
    @Transactional
    public EnrollmentResponseDto updateEnrollmentStatus(Long id, EnrollmentStatus status) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
        
        enrollment.setStatus(status);
        if (status == EnrollmentStatus.COMPLETED && enrollment.getCompletionDate() == null) {
            enrollment.setCompletionDate(LocalDate.now());
        }
        
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponseDto(updatedEnrollment);
    }
    
    @Override
    @Transactional
    public void deleteEnrollment(Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Enrollment not found with id: " + id);
        }
        enrollmentRepository.deleteById(id);
    }
    
    @Override
    public EnrollmentResponseDto getEnrollmentByStudentCode(String studentCode) {
        Enrollment enrollment = enrollmentRepository.findByStudentCode(studentCode)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with student code: " + studentCode));
        return enrollmentMapper.toResponseDto(enrollment);
    }
    
    @Override
    public EnrollmentResponseDto getEnrollmentByStudentCodeAndYear(String studentCode, String academicYear) {
        Enrollment enrollment = enrollmentRepository.findByStudentCodeAndAcademicYear(studentCode, academicYear)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enrollment not found with student code: " + studentCode + " and year: " + academicYear));
        return enrollmentMapper.toResponseDto(enrollment);
    }
    
}
