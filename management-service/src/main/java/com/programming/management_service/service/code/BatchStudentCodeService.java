package com.programming.management_service.service.code;

import com.programming.common.common_dto.student.StudentResponseDto;
import com.programming.common.exception.ResourceNotFoundException;
import com.programming.management_service.domain.model.Classroom;
import com.programming.management_service.domain.model.Enrollment;
import com.programming.management_service.management_caller.StudentClient;
import com.programming.management_service.repository.ClassroomRepository;
import com.programming.management_service.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Collator;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

/**
service for generating student codes in batch for enrollments in a classroom
after enrollments have been created, confirmed stable

 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BatchStudentCodeService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final ClassroomRepository classroomRepository;
    private final StudentCodeGenerator studentCodeGenerator;
    private final StudentClient studentClient;
    
    //generate codes in alphabet order in a year
    @Transactional
    public BatchGenerateResult generateCodesForClassroom(
            Long classroomId, 
            String academicYear, 
            boolean forceRegenerate) {
        
        log.info("Starting batch code generation for classroomId: {}, year: {}, force: {}", 
                classroomId, academicYear, forceRegenerate);
        
        // classroom exists
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + classroomId));
        
        // get enrollments in the classroom for the year
        List<Enrollment> enrollments = enrollmentRepository
                .findByClassroomIdAndAcademicYearOrderByStudentCodeAsc(classroomId, academicYear);
        
        if (enrollments.isEmpty()) {
            log.warn("No enrollments found for classroomId: {}, year: {}", classroomId, academicYear);
            return BatchGenerateResult.builder()
                    .classroomId(classroomId)
                    .classroomName(classroom.getName())
                    .academicYear(academicYear)
                    .totalEnrollments(0)
                    .generatedCount(0)
                    .skippedCount(0)
                    .build();
        }
        
        // filter enrollments based on forceRegenerate flag
        List<Enrollment> enrollmentsToProcess;
        int skippedCount = 0;
        
        if (forceRegenerate) {
            enrollmentsToProcess = enrollments;
        } else {
            enrollmentsToProcess = enrollments.stream()
                    .filter(e -> e.getStudentCode() == null || e.getStudentCode().trim().isEmpty())
                    .collect(Collectors.toList());
            skippedCount = enrollments.size() - enrollmentsToProcess.size();
        }
        
        if (enrollmentsToProcess.isEmpty()) {
            log.info("All enrollments already have student codes. Use forceRegenerate=true to regenerate.");
            return BatchGenerateResult.builder()
                    .classroomId(classroomId)
                    .classroomName(classroom.getName())
                    .academicYear(academicYear)
                    .totalEnrollments(enrollments.size())
                    .generatedCount(0)
                    .skippedCount(skippedCount)
                    .build();
        }
        
        // get student information from user-service
        Set<Long> studentIds = enrollmentsToProcess.stream()
                .map(Enrollment::getStudentId)
                .collect(Collectors.toSet());
        
        List<StudentResponseDto> students = studentClient.getStudentsByIds(studentIds);
        
        // create map for easy lookup
        Map<Long, StudentResponseDto> studentMap = students.stream()
                .collect(Collectors.toMap(StudentResponseDto::getId, s -> s));
        
        // sort enrollments by student fullName (alphabet - Vietnamese standard)
        // Priority: (last name) → (middle) → (first)
        // using collator
        Collator viCollator = Collator.getInstance(new Locale("vi", "VN"));
        viCollator.setStrength(Collator.TERTIARY);
        
        List<EnrollmentWithStudent> enrollmentWithStudents = enrollmentsToProcess.stream()
                .map(e -> {
                    StudentResponseDto student = studentMap.get(e.getStudentId());
                    if (student == null) {
                        log.warn("Student not found for id: {}. Skipping enrollment.", e.getStudentId());
                        return null;
                    }
                    return new EnrollmentWithStudent(e, student);
                })
                .filter(Objects::nonNull)
                .sorted((ews1, ews2) -> {
                    String name1 = ews1.getStudent().getFullName();
                    String name2 = ews2.getStudent().getFullName();
                    
                    String lastName1 = getLastName(name1);
                    String lastName2 = getLastName(name2);
                    
                    int cmp = viCollator.compare(lastName1, lastName2);
                    if (cmp != 0) return cmp;
                    
                    // compare full names if last names are equal
                    return viCollator.compare(name1, name2);
                })
                .collect(Collectors.toList());
        
        // generate student codes sequentially
        int sequence = 1;
        List<String> generatedCodes = new ArrayList<>();
        
        for (EnrollmentWithStudent ews : enrollmentWithStudents) {
            String studentCode = studentCodeGenerator.generateStudentCodeWithSequence(
                    classroomId, 
                    academicYear, 
                    sequence
            );
            
            ews.getEnrollment().setStudentCode(studentCode);
            enrollmentRepository.save(ews.getEnrollment());
            
            generatedCodes.add(String.format("%03d. %s - %s", 
                    sequence, 
                    ews.getStudent().getFullName(), 
                    studentCode));
            
            log.info("Generated code: {} for student: {} ({})", 
                    studentCode, 
                    ews.getStudent().getFullName(), 
                    ews.getStudent().getId());
            
            sequence++;
        }
        
        log.info("Batch code generation completed. Generated {} codes for classroom: {}", 
                enrollmentWithStudents.size(), classroom.getName());
        
        return BatchGenerateResult.builder()
                .classroomId(classroomId)
                .classroomName(classroom.getName())
                .academicYear(academicYear)
                .totalEnrollments(enrollments.size())
                .generatedCount(enrollmentWithStudents.size())
                .skippedCount(skippedCount)
                .generatedCodes(generatedCodes)
                .build();
    }

    
    // extract last name from full name
    private String getLastName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "";
        }
        String[] parts = fullName.trim().split("\\s+");
        return parts[parts.length - 1];
    }
    
    // @Deprecated
    // private String createVietnameseSortKey(String fullName) {
    //     if (fullName == null || fullName.trim().isEmpty()) {
    //         return "";
    //     }
        
    //     String normalized = normalizeVietnamese(fullName);
        
    //     String[] parts = normalized.split("\\s+");
        
    //     if (parts.length == 0) {
    //         return "";
    //     }
        
    //     if (parts.length == 1) {
    //         return parts[0];
    //     }
        
    //     StringBuilder sortKey = new StringBuilder();
        
    //     String ten = parts[parts.length - 1];
    //     sortKey.append(ten);
        
    //     for (int i = parts.length - 2; i >= 1; i--) {
    //         sortKey.append("|");
    //         sortKey.append(parts[i]);
    //     }

    //     sortKey.append("|");
    //     sortKey.append(parts[0]);
        
    //     return sortKey.toString();
    // }
    
    // @Deprecated
    // private String normalizeVietnamese(String input) {
    //     if (input == null) return "";
        
    //     String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
    //     normalized = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    //     return normalized.toLowerCase().trim();
    // }

    private static class EnrollmentWithStudent {
        private final Enrollment enrollment;
        private final StudentResponseDto student;
        
        public EnrollmentWithStudent(Enrollment enrollment, StudentResponseDto student) {
            this.enrollment = enrollment;
            this.student = student;
        }
        
        public Enrollment getEnrollment() {
            return enrollment;
        }
        
        public StudentResponseDto getStudent() {
            return student;
        }
    }

    @lombok.Data
    @lombok.Builder
    public static class BatchGenerateResult {
        private Long classroomId;
        private String classroomName;
        private String academicYear;
        private Integer totalEnrollments;
        private Integer generatedCount;
        private Integer skippedCount;
        private List<String> generatedCodes; // For display/logging
    }
}
