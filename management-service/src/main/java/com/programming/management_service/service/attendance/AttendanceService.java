package com.programming.management_service.service.attendance;

import com.programming.management_service.domain.dto.request.AttendanceRecordRequestDto;
import com.programming.management_service.domain.dto.request.AttendanceSessionRequestDto;
import com.programming.management_service.domain.dto.response.AttendanceRecordResponseDto;
import com.programming.management_service.domain.dto.response.AttendanceSessionResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    
    // Session
    AttendanceSessionResponseDto createSession(AttendanceSessionRequestDto dto, Long createdBy);

    AttendanceSessionResponseDto getSessionById(Long id);

    List<AttendanceSessionResponseDto> getSessionsByClassroom(Long classroomId);

    List<AttendanceSessionResponseDto> getSessionsByClassroomAndDate(Long classroomId, LocalDate sessionDate);

    AttendanceSessionResponseDto updateSession(Long id, AttendanceSessionRequestDto dto);

    AttendanceSessionResponseDto closeSession(Long id);

    AttendanceSessionResponseDto cancelSession(Long id);

    void deleteSession(Long id);
    
    // Attendance Record Management
  
    AttendanceRecordResponseDto createRecord(AttendanceRecordRequestDto dto, Long recordedBy);

    AttendanceRecordResponseDto getRecordById(Long id);
  
    List<AttendanceRecordResponseDto> getRecordsBySession(Long sessionId);

    List<AttendanceRecordResponseDto> getRecordsByStudent(String studentCode);

    AttendanceRecordResponseDto updateRecord(Long id, AttendanceRecordRequestDto dto);

    void deleteRecord(Long id);

    boolean isStudentAttended(Long sessionId, String studentCode);

        
    // Batch Operations

    List<AttendanceRecordResponseDto> createBatchRecords(List<AttendanceRecordRequestDto> dtos, Long recordedBy);

    List<AttendanceSessionResponseDto> autoCreateSessionsForDate(LocalDate date);
    
    // Statistics & Reports

    AttendanceSessionResponseDto getSessionWithStatistics(Long sessionId);

    List<AttendanceSessionResponseDto> getSessionsWithStatisticsInDateRange(
            Long classroomId, LocalDate startDate, LocalDate endDate);

    // adjust later
    AttendanceRecordResponseDto getStudentAttendanceReport(
            String studentCode, LocalDate startDate, LocalDate endDate);

    List<String> getFrequentAbsentStudents(Long classroomId, LocalDate startDate, LocalDate endDate, int threshold);

    Double getClassroomAttendanceRate(Long classroomId, LocalDate startDate, LocalDate endDate);
}
