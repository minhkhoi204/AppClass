package com.programming.management_service.repository;

import com.programming.management_service.domain.model.Enrollment;
import com.programming.management_service.domain.model.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByClassroomId(Long classroomId);

    List<Enrollment> findByClassroomIdAndStatus(Long classroomId, EnrollmentStatus status);
    
    Optional<Enrollment> findByStudentIdAndClassroomIdAndStatus(
            Long studentId, Long classroomId, EnrollmentStatus status);
    
    List<Enrollment> findByStudentIdAndAcademicYear(Long studentId, String academicYear);

    List<Enrollment> findByClassroomIdAndAcademicYear(Long classroomId, String academicYear);
    
    boolean existsByStudentIdAndClassroomIdAndAcademicYear(
            Long studentId, Long classroomId, String academicYear);

    long countByClassroomIdAndStatus(Long classroomId, EnrollmentStatus status);

    long countByClassroomIdAndAcademicYear(Long classroomId, String academicYear);

    List<Enrollment> findByAcademicYear(String academicYear);

    List<Enrollment> findByAcademicYearAndStatus(String academicYear, EnrollmentStatus status);

    List<Enrollment> findByClassroomIdAndStudentIdIn(Long classroomId, List<Long> studentIds);

    // student code related
    
    @Query("SELECT e.studentId FROM Enrollment e WHERE e.classroomId = :classroomId AND e.academicYear = :academicYear")
    List<Long> findStudentIdsByClassroomAndYear(
            @Param("classroomId") Long classroomId, 
            @Param("academicYear") String academicYear);

    @Modifying
    @Query("UPDATE Enrollment e SET e.status = :newStatus, e.updatedAt = CURRENT_TIMESTAMP, e.updatedBy = :updatedBy " +
           "WHERE e.academicYear = :academicYear AND e.status = :currentStatus")
    int updateStatusByAcademicYear(
            @Param("academicYear") String academicYear,
            @Param("currentStatus") EnrollmentStatus currentStatus,
            @Param("newStatus") EnrollmentStatus newStatus,
            @Param("updatedBy") Long updatedBy);

    @Modifying
    @Query("UPDATE Enrollment e SET e.status = :newStatus, e.updatedAt = CURRENT_TIMESTAMP, e.updatedBy = :updatedBy " +
           "WHERE e.classroomId = :classroomId AND e.academicYear = :academicYear AND e.status = :currentStatus")
    int updateStatusByClassroomAndYear(
            @Param("classroomId") Long classroomId,
            @Param("academicYear") String academicYear,
            @Param("currentStatus") EnrollmentStatus currentStatus,
            @Param("newStatus") EnrollmentStatus newStatus,
            @Param("updatedBy") Long updatedBy);

    Optional<Enrollment> findByStudentCodeAndAcademicYear(String studentCode, String academicYear);

    boolean existsByStudentCodeAndAcademicYear(String studentCode, String academicYear);

    long countByClassroomIdAndAcademicYearAndStatus(
            Long classroomId, 
            String academicYear, 
            EnrollmentStatus status);

    @Query("SELECT CAST(SUBSTRING(e.studentCode, LENGTH(e.studentCode) - 2, 3) AS int) " +
           "FROM Enrollment e " +
           "WHERE e.classroomId = :classroomId " +
           "AND e.academicYear = :academicYear " +
           "ORDER BY e.studentCode DESC")
    List<Integer> findMaxStudentCodeSequenceByClassroomAndYear(
            @Param("classroomId") Long classroomId,
            @Param("academicYear") String academicYear);

    //end of year

    List<Enrollment> findByClassroomIdAndAcademicYearAndStatus(
            Long classroomId, 
            String academicYear, 
            EnrollmentStatus status);

    @Query("SELECT e FROM Enrollment e " +
           "WHERE e.academicYear = :academicYear " +
           "AND e.status IN :statuses " +
           "ORDER BY e.classroomId, e.studentCode")
    List<Enrollment> findByAcademicYearAndStatusIn(
            @Param("academicYear") String academicYear,
            @Param("statuses") List<EnrollmentStatus> statuses);
    
    @Query("SELECT e.status, COUNT(e) FROM Enrollment e " +
           "WHERE e.academicYear = :academicYear " +
           "GROUP BY e.status")
    List<Object[]> countByStatusForAcademicYear(@Param("academicYear") String academicYear);
    
    @Query("SELECT e.classroomId, e.status, COUNT(e) FROM Enrollment e " +
           "WHERE e.academicYear = :academicYear " +
           "GROUP BY e.classroomId, e.status")
    List<Object[]> countByClassroomAndStatusForAcademicYear(@Param("academicYear") String academicYear);
}
