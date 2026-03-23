package com.programming.management_service.service.code;

import com.programming.management_service.constants.ClassCodeMapping;
import com.programming.management_service.domain.model.Classroom;
import com.programming.management_service.domain.model.Enrollment;
import com.programming.management_service.repository.ClassroomRepository;
import com.programming.management_service.repository.EnrollmentRepository;
import com.programming.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentCodeGenerator {
    
    private final EnrollmentRepository enrollmentRepository;
    private final ClassroomRepository classroomRepository;
    
    public String generateStudentCode(Long classroomId, String academicYear) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + classroomId));
        
        String yearCode = extractYearCode(academicYear);
        
        String classCode = ClassCodeMapping.getClassCode(classroom.getName());
        
        int sequence = getNextSequenceNumber(classroomId, academicYear);
        
        String studentCode = String.format("%s-%s-%03d", yearCode, classCode, sequence);
        
        log.info("Generated student code: {} for classroom: {}, year: {}", 
                studentCode, classroom.getName(), academicYear);
        
        return studentCode;
    }
    
    public String generateStudentCodeWithSequence(Long classroomId, String academicYear, int sequence) {
        if (sequence < 1 || sequence > 999) {
            throw new IllegalArgumentException("Sequence must be between 1 and 999");
        }
        
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + classroomId));
        
        String yearCode = extractYearCode(academicYear);
        
        String classCode = ClassCodeMapping.getClassCode(classroom.getName());
        
        String studentCode = String.format("%s-%s-%03d", yearCode, classCode, sequence);
        
        log.debug("Generated student code with fixed sequence: {} for classroom: {}", 
                studentCode, classroom.getName());
        
        return studentCode;
    }
    
    private String extractYearCode(String academicYear) {
        if (academicYear == null || academicYear.trim().isEmpty()) {
            throw new IllegalArgumentException("Academic year cannot be null or empty");
        }
        
        Pattern pattern = Pattern.compile("^(\\d{4})");
        Matcher matcher = pattern.matcher(academicYear.trim());
        
        if (matcher.find()) {
            String fullYear = matcher.group(1);
            return fullYear.substring(2);
        }
        
        throw new IllegalArgumentException("Invalid academic year format: " + academicYear + 
                ". Expected format: '2026-2027' or '2026'");
    }
    

    //use generateStudentCodeWithSequence() for multiple generations

    private int getNextSequenceNumber(Long classroomId, String academicYear) {

        List<Enrollment> existingEnrollments = enrollmentRepository
                .findByClassroomIdAndAcademicYearOrderByStudentCodeAsc(classroomId, academicYear);
        
        if (existingEnrollments.isEmpty()) {
            return 1;
        }
        
        int maxSequence = 0;
        Pattern pattern = Pattern.compile("-(\\d{3})$"); 
        
        for (Enrollment enrollment : existingEnrollments) {
            String code = enrollment.getStudentCode();
            
            if (code == null || code.trim().isEmpty()) {
                continue;
            }
            
            Matcher matcher = pattern.matcher(code);
            
            if (matcher.find()) {
                int seq = Integer.parseInt(matcher.group(1));
                if (seq > maxSequence) {
                    maxSequence = seq;
                }
            }
        }
        
        return maxSequence + 1;
    }
    
    public boolean isValidStudentCodeFormat(String studentCode) {
        if (studentCode == null || studentCode.trim().isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^\\d{2}-[A-Z]{2,4}-\\d{1,2}-\\d{3}$");
        return pattern.matcher(studentCode.trim()).matches();
    }

    public StudentCodeInfo parseStudentCode(String studentCode) {
        if (!isValidStudentCodeFormat(studentCode)) {
            throw new IllegalArgumentException("Invalid student code format: " + studentCode);
        }
        
        String[] parts = studentCode.split("-");
        
        return StudentCodeInfo.builder()
                .yearCode(parts[0])
                .classCode(parts[1] + "-" + parts[2])
                .sequence(Integer.parseInt(parts[3]))
                .fullCode(studentCode)
                .build();
    }
    

    @lombok.Data
    @lombok.Builder
    public static class StudentCodeInfo {
        private String yearCode;      // "26"
        private String classCode;     // "KT-2"
        private Integer sequence;     // 35
        private String fullCode;      // "26-KT-2-035"
    }
}
