package com.programming.management_service.repository;

import com.programming.management_service.domain.model.TransitionStaging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransitionStagingRepository extends JpaRepository<TransitionStaging, Long> {

    List<TransitionStaging> findByOldClassroomIdAndOldAcademicYearAndIsFinalized(
            Long oldClassroomId, 
            String oldAcademicYear, 
            Boolean isFinalized);

    List<TransitionStaging> findByOldClassroomIdAndOldAcademicYear(
            Long oldClassroomId, 
            String oldAcademicYear);

    List<TransitionStaging> findByStudentIdOrderByCreatedAtDesc(Long studentId);

    Optional<TransitionStaging> findByStudentIdAndOldAcademicYear(
            Long studentId, 
            String oldAcademicYear);
    
    boolean existsByStudentIdAndOldAcademicYearAndIsFinalized(
            Long studentId, 
            String oldAcademicYear, 
            Boolean isFinalized);

    List<TransitionStaging> findByNewClassroomIdAndNewAcademicYearAndIsFinalized(
            Long newClassroomId, 
            String newAcademicYear, 
            Boolean isFinalized);

    int countByNewClassroomIdAndNewAcademicYearAndIsFinalized(
            Long newClassroomId, 
            String newAcademicYear, 
            Boolean isFinalized);

    void deleteByOldClassroomIdAndOldAcademicYearAndIsFinalized(
            Long oldClassroomId, 
            String oldAcademicYear, 
            Boolean isFinalized);
    
    List<TransitionStaging> findByOldAcademicYearAndIsFinalized(
            String oldAcademicYear, 
            Boolean isFinalized);
    
    List<TransitionStaging> findByOldClassroomIdInAndOldAcademicYearAndIsFinalized(
            List<Long> oldClassroomIds, 
            String oldAcademicYear, 
            Boolean isFinalized);
}
