package com.programming.management_service.repository;

import com.programming.management_service.domain.model.Enrollment;
import com.programming.management_service.domain.model.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    List<Enrollment> findByStudentId(Long studentId);
    
    List<Enrollment> findByClassroomId(Long classroomId);
    
    List<Enrollment> findByClassroomIdAndStatus(Long classroomId, EnrollmentStatus status);
    
    Optional<Enrollment> findByStudentIdAndClassroomIdAndStatus(
            Long studentId, Long classroomId, EnrollmentStatus status);
    
    List<Enrollment> findByStudentIdAndAcademicYear(Long studentId, String academicYear);
    
    List<Enrollment> findByClassroomIdAndAcademicYear(Long classroomId, String academicYear);
    
    boolean existsByClassroomIdAndAcademicYearAndStudentCode(
            Long classroomId, String academicYear, String studentCode);
    
    // find active enrollment by classroom, academic year and student code
    Optional<Enrollment> findByClassroomIdAndAcademicYearAndStudentCodeAndStatus(
            Long classroomId, String academicYear, String studentCode, EnrollmentStatus status);
    
    long countByClassroomIdAndStatus(Long classroomId, EnrollmentStatus status);
    
    // get next student number for classroom in academic year
    @Query("SELECT MAX(CAST(SUBSTRING(e.studentCode, 1, 2) AS int)) " +
           "FROM Enrollment e " +
           "WHERE e.classroomId = :classroomId AND e.academicYear = :academicYear")
    Integer findMaxStudentNumberInClassroom(
            @Param("classroomId") Long classroomId,
            @Param("academicYear") String academicYear);
}
