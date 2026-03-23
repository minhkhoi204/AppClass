package com.programming.management_service.repository;

import com.programming.management_service.domain.model.AssignmentStatus;
import com.programming.management_service.domain.model.ClassroomAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomAssignmentRepository extends JpaRepository<ClassroomAssignment, Long> {
    
    List<ClassroomAssignment> findByCatechistId(Long catechistId);

    List<ClassroomAssignment> findByClassroomId(Long classroomId);

    List<ClassroomAssignment> findByClassroomIdAndAcademicYear(Long classroomId, String academicYear);

    List<ClassroomAssignment> findByClassroomIdAndAcademicYearAndStatus(
            Long classroomId, String academicYear, AssignmentStatus status);

    List<ClassroomAssignment> findByCatechistIdAndAcademicYear(Long catechistId, String academicYear);

    List<ClassroomAssignment> findByCatechistIdAndAcademicYearAndStatus(
            Long catechistId, String academicYear, AssignmentStatus status);

    Optional<ClassroomAssignment> findByCatechistIdAndClassroomIdAndAcademicYear(
            Long catechistId, Long classroomId, String academicYear);

    boolean existsByCatechistIdAndClassroomIdAndAcademicYear(
            Long catechistId, Long classroomId, String academicYear);

    Integer countByClassroomIdAndAcademicYearAndStatus(
            Long classroomId, String academicYear, AssignmentStatus status);

    Integer countByCatechistIdAndAcademicYearAndStatus(
            Long catechistId, String academicYear, AssignmentStatus status);
}
