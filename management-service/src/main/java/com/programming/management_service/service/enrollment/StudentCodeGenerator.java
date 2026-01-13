package com.programming.management_service.service.enrollment;

import com.programming.management_service.domain.model.Classroom;
import com.programming.management_service.domain.model.EnrollmentStatus;
import com.programming.management_service.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//26-TS-1-001
@Service
@RequiredArgsConstructor
public class StudentCodeGenerator {
    
    private final EnrollmentRepository enrollmentRepository;
    
    private static final Map<String, String> CLASS_NAME_ABBREVIATION = new HashMap<>();
    
    static {
        CLASS_NAME_ABBREVIATION.put("Cỏ non", "CN");
        CLASS_NAME_ABBREVIATION.put("Khai tâm", "KT");
        CLASS_NAME_ABBREVIATION.put("Rước lễ", "RL");
        CLASS_NAME_ABBREVIATION.put("Thêm sức", "TS");
        CLASS_NAME_ABBREVIATION.put("Bao đồng", "BD");
        CLASS_NAME_ABBREVIATION.put("Vào đời", "VD");
        CLASS_NAME_ABBREVIATION.put("Dự trưởng", "DT");
    }
    
    public String generateStudentCode(Classroom classroom, String academicYear) {
        String yearPrefix = extractYearPrefix(academicYear);
        
        String classCode = extractClassCode(classroom.getName());
        
        int sequence = getNextSequence(classroom.getId(), academicYear);
        
        //format
        return String.format("%s-%s-%03d", yearPrefix, classCode, sequence);
    }
    
    private String extractYearPrefix(String academicYear) {
        if (academicYear == null || academicYear.isEmpty()) {
            throw new IllegalArgumentException("Academic year không được rỗng");
        }
        
        String[] parts = academicYear.split("-");
        String startYear = parts[0].trim();
        
        if (startYear.length() >= 2) {
            return startYear.substring(startYear.length() - 2);
        }
        
        return startYear;
    }

    private String extractClassCode(String className) {
        if (className == null || className.isEmpty()) {
            throw new IllegalArgumentException("Tên lớp không được rỗng");
        }
        
        Pattern pattern = Pattern.compile("^(.+?)\\s*(\\d+)?$");
        Matcher matcher = pattern.matcher(className.trim());
        
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Tên lớp không hợp lệ: " + className);
        }
        
        String baseName = matcher.group(1).trim();
        String level = matcher.group(2);
        
        String abbreviation = CLASS_NAME_ABBREVIATION.get(baseName);
        if (abbreviation == null) {
            throw new IllegalArgumentException("Không tìm thấy viết tắt cho lớp: " + baseName);
        }
        
        String levelCode = (level != null && !level.isEmpty()) ? level : "0";
        
        return abbreviation + "-" + levelCode;
    }
    
    private int getNextSequence(Long classroomId, String academicYear) {
        List<Integer> sequences = enrollmentRepository
                .findMaxStudentCodeSequenceByClassroomAndYear(classroomId, academicYear);
        
        if (sequences.isEmpty()) {
            return 1;
        }
        
        Integer maxSequence = sequences.get(0);
        return (maxSequence != null ? maxSequence : 0) + 1;
    }

    public boolean isValidStudentCode(String studentCode) {
        if (studentCode == null || studentCode.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^\\d{2}-[A-Z]{2,3}-\\d-\\d{3}$");
        return pattern.matcher(studentCode).matches();
    }
    
    public Map<String, String> parseStudentCode(String studentCode) {
        if (!isValidStudentCode(studentCode)) {
            throw new IllegalArgumentException("Student code không hợp lệ: " + studentCode);
        }
        
        String[] parts = studentCode.split("-");
        
        Map<String, String> info = new HashMap<>();
        info.put("year", parts[0]);
        info.put("classAbbr", parts[1]);
        info.put("level", parts[2]);
        info.put("sequence", parts[3]);
        
        return info;
    }
}
