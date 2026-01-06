package com.programming.management_service.repository;

import com.programming.management_service.domain.model.AttendanceRecord;
import com.programming.management_service.domain.model.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    
    // ex: session would be before and after class, total: 2
    // each students would have 2 records per day as before and after class
    List<AttendanceRecord> findByAttendanceSessionId(Long attendanceSessionId);

    Optional<AttendanceRecord> findByAttendanceSessionIdAndEnrollmentId(
            Long attendanceSessionId, 
            Long enrollmentId
    );

    List<AttendanceRecord> findByEnrollmentId(Long enrollmentId);

    List<AttendanceRecord> findByAttendanceSessionIdAndStatus(
            Long attendanceSessionId, 
            AttendanceStatus status
    );
    
    @Query("SELECT COUNT(a) FROM AttendanceRecord a " +
           "WHERE a.attendanceSessionId = :sessionId AND a.status = :status")
    long countBySessionIdAndStatus(
            @Param("sessionId") Long sessionId,
            @Param("status") AttendanceStatus status
    );
    
    long countByAttendanceSessionId(Long attendanceSessionId);
    
    boolean existsByAttendanceSessionIdAndEnrollmentId(Long attendanceSessionId, Long enrollmentId);
    
    void deleteByAttendanceSessionId(Long attendanceSessionId);
    
    // get attendance records of a student in specific sessions
    @Query("SELECT a FROM AttendanceRecord a " +
           "WHERE a.enrollmentId = :enrollmentId " +
           "AND a.attendanceSessionId IN :sessionIds " +
           "ORDER BY a.createdAt DESC")
    List<AttendanceRecord> findEnrollmentAttendanceHistory(
            @Param("enrollmentId") Long enrollmentId,
            @Param("sessionIds") List<Long> sessionIds
    );
}
