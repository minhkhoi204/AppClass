package com.programming.management_service.repository;

import com.programming.management_service.domain.model.AttendanceSession;
import com.programming.management_service.domain.model.SessionStatus;
import com.programming.management_service.domain.model.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {

    List<AttendanceSession> findByClassroomId(Long classroomId);
    
    List<AttendanceSession> findByClassroomIdAndSessionDate(Long classroomId, LocalDate sessionDate);
   
    Optional<AttendanceSession> findByClassroomIdAndSessionDateAndSessionType(
            Long classroomId, 
            LocalDate sessionDate, 
            SessionType sessionType
    );

    List<AttendanceSession> findByStatus(SessionStatus status);

    List<AttendanceSession> findByClassroomIdAndStatus(Long classroomId, SessionStatus status);

    // find session by classroom and specific date range
    @Query("SELECT a FROM AttendanceSession a WHERE a.classroomId = :classroomId " +
           "AND a.sessionDate BETWEEN :startDate AND :endDate " +
           "ORDER BY a.sessionDate DESC, a.sessionType")
    List<AttendanceSession> findByClassroomIdAndDateRange(
            @Param("classroomId") Long classroomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
 
    boolean existsByClassroomIdAndSessionDateAndSessionType(
            Long classroomId,
            LocalDate sessionDate,
            SessionType sessionType
    );
}
