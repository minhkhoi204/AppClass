package com.programming.management_service.repository;

import com.programming.management_service.domain.model.Enrollment;
import com.programming.management_service.domain.model.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    List<Enrollment> findByStudentId(Long studentId);
    
    List<Enrollment> findByClassroomId(Long classroomId);
    
    List<Enrollment> findByClassroomIdAndAcademicYear(Long classroomId, String academicYear);
    
    int countByClassroomIdAndAcademicYear(Long classroomId, String academicYear);
    
    Optional<Enrollment> findByStudentIdAndClassroomIdAndAcademicYear(Long studentId, Long classroomId, String academicYear);
    
    List<Enrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status);
    
    List<Enrollment> findByClassroomIdAndStatus(Long classroomId, EnrollmentStatus status);
    
    boolean existsByStudentIdAndClassroomIdAndAcademicYear(Long studentId, Long classroomId, String academicYear);
    
    Optional<Enrollment> findByStudentCode(String studentCode);
    
    Optional<Enrollment> findByStudentCodeAndAcademicYear(String studentCode, String academicYear);
    
    boolean existsByStudentCode(String studentCode);
    
    boolean existsByStudentCodeAndAcademicYear(String studentCode, String academicYear);
    
    // Lấy sequence number cuối cùng trong lớp (để generate code tiếp theo)
    List<Enrollment> findByClassroomIdAndAcademicYearOrderByStudentCodeAsc(Long classroomId, String academicYear);
}
