package com.programming.management_service.repository.transition;

import com.programming.management_service.domain.model.transition.ClassTransitionStaging;
import com.programming.management_service.domain.model.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassTransitionStagingRepository extends JpaRepository<ClassTransitionStaging, Long> {
    
    Optional<ClassTransitionStaging> findByStudentIdAndNewAcademicYear(Long studentId, String newAcademicYear);

    List<ClassTransitionStaging> findByNewClassroomIdAndNewAcademicYear(Long newClassroomId, String newAcademicYear);

    List<ClassTransitionStaging> findByNewAcademicYear(String newAcademicYear);

    List<ClassTransitionStaging> findByNewAcademicYearAndIsFinalized(String newAcademicYear, Boolean isFinalized);

    boolean existsByStudentIdAndNewAcademicYear(Long studentId, String newAcademicYear);

    @Modifying
    @Query("UPDATE ClassTransitionStaging s " +
           "SET s.isFinalized = true, s.finalizedBy = :finalizedBy, s.finalizedAt = CURRENT_TIMESTAMP " +
           "WHERE s.newAcademicYear = :newAcademicYear AND s.isFinalized = false")
    int finalizeAllForAcademicYear(
            @Param("newAcademicYear") String newAcademicYear,
            @Param("finalizedBy") Long finalizedBy);

    @Modifying
    @Query("DELETE FROM ClassTransitionStaging s " +
           "WHERE s.newAcademicYear = :newAcademicYear AND s.isFinalized = false")
    int deleteUnfinalizedForAcademicYear(@Param("newAcademicYear") String newAcademicYear);
    
    // advanced methods
    
//    List<ClassTransitionStaging> findByStudentId(Long studentId);
//    
//    long countByNewClassroomIdAndNewAcademicYearAndIsFinalized(
//            Long newClassroomId, 
//            String newAcademicYear, 
//            Boolean isFinalized);
//
//    List<ClassTransitionStaging> findByOldClassroomIdAndOldAcademicYear(Long oldClassroomId, String oldAcademicYear);
//
//    List<ClassTransitionStaging> findByOldAcademicYearAndOldStatus(String oldAcademicYear, EnrollmentStatus oldStatus);
//
//    @Query("SELECT s.oldStatus, COUNT(s) FROM ClassTransitionStaging s " +
//           "WHERE s.newAcademicYear = :newAcademicYear " +
//           "GROUP BY s.oldStatus")
//    List<Object[]> countByOldStatusForNewYear(@Param("newAcademicYear") String newAcademicYear);
//
//    @Query("SELECT s.newClassroomId, COUNT(s) FROM ClassTransitionStaging s " +
//           "WHERE s.newAcademicYear = :newAcademicYear " +
//           "AND s.isFinalized = false " +
//           "GROUP BY s.newClassroomId")
//    List<Object[]> countByNewClassroomForNewYear(@Param("newAcademicYear") String newAcademicYear);
//
//    @Modifying
//    @Query("UPDATE ClassTransitionStaging s " +
//           "SET s.isFinalized = true, s.finalizedBy = :finalizedBy, s.finalizedAt = CURRENT_TIMESTAMP " +
//           "WHERE s.newClassroomId = :newClassroomId " +
//           "AND s.newAcademicYear = :newAcademicYear " +
//           "AND s.isFinalized = false")
//    int finalizeByClassroom(
//            @Param("newClassroomId") Long newClassroomId,
//            @Param("newAcademicYear") String newAcademicYear,
//            @Param("finalizedBy") Long finalizedBy);
}
