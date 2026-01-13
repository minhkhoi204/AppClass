package com.programming.management_service.service.enrollment;

import com.programming.management_service.domain.model.Classroom;
import com.programming.management_service.domain.model.Enrollment;
import com.programming.management_service.domain.model.EnrollmentStatus;
import com.programming.management_service.repository.ClassroomRepository;
import com.programming.management_service.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final ClassroomRepository classroomRepository;
    private final StudentCodeGenerator studentCodeGenerator;
    
    @Transactional
    public Enrollment enrollStudent(Long studentId, Long classroomId, String academicYear, Long createdBy) {
        if (enrollmentRepository.existsByStudentIdAndClassroomIdAndAcademicYear(studentId, classroomId, academicYear)) {
            throw new IllegalStateException("Học sinh đã được ghi danh vào lớp này");
        }
        
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("Lớp học không tồn tại"));
        
        String studentCode = studentCodeGenerator.generateStudentCode(classroom, academicYear);
        
        Enrollment enrollment = Enrollment.builder()
                .studentId(studentId)
                .classroomId(classroomId)
                .academicYear(academicYear)
                .studentCode(studentCode)
                .status(EnrollmentStatus.ACTIVE)
                .enrollmentDate(LocalDate.now())
                .createdBy(createdBy)
                .build();
        
        return enrollmentRepository.save(enrollment);
    }
    
    @Transactional
    public Enrollment updateEnrollmentStatus(Long enrollmentId, EnrollmentStatus newStatus, String note, Long updatedBy) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment không tồn tại"));
        
        enrollment.setStatus(newStatus);
        enrollment.setNote(note);
        enrollment.setUpdatedBy(updatedBy);
        
        if (newStatus == EnrollmentStatus.COMPLETED || newStatus == EnrollmentStatus.DROPPED) {
            enrollment.setCompletionDate(LocalDate.now());
        }
        
        return enrollmentRepository.save(enrollment);
    }
    
    public List<Enrollment> getEnrollmentsByClassroom(Long classroomId, String academicYear) {
        return enrollmentRepository.findByClassroomIdAndAcademicYear(classroomId, academicYear);
    }
    
    public List<Enrollment> getEnrollmentsByClassroomAndStatus(Long classroomId, String academicYear, EnrollmentStatus status) {
        return enrollmentRepository.findByClassroomIdAndAcademicYearAndStatus(classroomId, academicYear, status);
    }
    
    public Enrollment findByStudentCode(String studentCode, String academicYear) {
        return enrollmentRepository.findByStudentCodeAndAcademicYear(studentCode, academicYear)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy enrollment với mã: " + studentCode));
    }
}
