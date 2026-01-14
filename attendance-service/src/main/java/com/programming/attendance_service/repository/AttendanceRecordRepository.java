package com.programming.attendance_service.repository;

import com.programming.attendance_service.domain.model.AttendanceRecord;
import com.programming.attendance_service.domain.model.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    
    // ex: session would be before and after class, total: 2
    // each students would have 2 records per day as before and after class
    List<AttendanceRecord> findByAttendanceSessionId(Long attendanceSessionId);

    Optional<AttendanceRecord> findByAttendanceSessionIdAndStudentId(
            Long attendanceSessionId, 
            Long studentId
    );

    List<AttendanceRecord> findByStudentId(Long studentId);

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
    
    boolean existsByAttendanceSessionIdAndStudentId(Long attendanceSessionId, Long studentId);
    
    void deleteByAttendanceSessionId(Long attendanceSessionId);
    
    // get attendance records of a student in specific sessions
    @Query("SELECT a FROM AttendanceRecord a " +
           "WHERE a.studentId = :studentId " +
           "AND a.attendanceSessionId IN :sessionIds " +
           "ORDER BY a.createdAt DESC")
    List<AttendanceRecord> findStudentAttendanceHistory(
            @Param("studentId") Long studentId,
            @Param("sessionIds") List<Long> sessionIds
    );
}
