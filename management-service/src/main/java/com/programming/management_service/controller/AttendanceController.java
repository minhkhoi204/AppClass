package com.programming.management_service.controller;

import com.programming.common.common_auth.JwtUtil;
import com.programming.common.response.ApiResponse;
import com.programming.management_service.domain.dto.request.AttendanceRecordRequestDto;
import com.programming.management_service.domain.dto.request.AttendanceSessionRequestDto;
import com.programming.management_service.domain.dto.response.AttendanceRecordResponseDto;
import com.programming.management_service.domain.dto.response.AttendanceSessionResponseDto;
import com.programming.management_service.service.attendance.AttendanceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final JwtUtil jwtUtil;

    // Session Management

    @PostMapping("/sessions")
    @PreAuthorize("hasAnyRole('HUYNH_TRUONG', 'DU_TRUONG', 'DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> createSession(
            @Valid @RequestBody AttendanceSessionRequestDto dto,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        AttendanceSessionResponseDto response = attendanceService.createSession(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Attendance session created successfully", response));
    }

    @GetMapping("/sessions/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getSessionById(@PathVariable Long id) {
        AttendanceSessionResponseDto response = attendanceService.getSessionById(id);
        return ResponseEntity.ok(new ApiResponse("Success", response));
    }

    @GetMapping("/sessions/classroom/{classroomId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getSessionsByClassroom(@PathVariable Long classroomId) {
        List<AttendanceSessionResponseDto> sessions = attendanceService.getSessionsByClassroom(classroomId);
        return ResponseEntity.ok(new ApiResponse("Success", sessions));
    }

    @GetMapping("/sessions/classroom/{classroomId}/date/{date}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getSessionsByClassroomAndDate(
            @PathVariable Long classroomId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AttendanceSessionResponseDto> sessions = attendanceService.getSessionsByClassroomAndDate(classroomId, date);
        return ResponseEntity.ok(new ApiResponse("Success", sessions));
    }

    @PutMapping("/sessions/{id}")
    @PreAuthorize("hasAnyRole('HUYNH_TRUONG', 'DU_TRUONG', 'DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> updateSession(
            @PathVariable Long id,
            @Valid @RequestBody AttendanceSessionRequestDto dto) {
        AttendanceSessionResponseDto response = attendanceService.updateSession(id, dto);
        return ResponseEntity.ok(new ApiResponse("Attendance session updated successfully", response));
    }

    @PatchMapping("/sessions/{id}/close")
    @PreAuthorize("hasAnyRole('HUYNH_TRUONG', 'DU_TRUONG', 'DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> closeSession(@PathVariable Long id) {
        AttendanceSessionResponseDto response = attendanceService.closeSession(id);
        return ResponseEntity.ok(new ApiResponse("Attendance session closed successfully", response));
    }

    @PatchMapping("/sessions/{id}/cancel")
    @PreAuthorize("hasAnyRole('HUYNH_TRUONG', 'DU_TRUONG', 'DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> cancelSession(@PathVariable Long id) {
        AttendanceSessionResponseDto response = attendanceService.cancelSession(id);
        return ResponseEntity.ok(new ApiResponse("Attendance session cancelled successfully", response));
    }

    @DeleteMapping("/sessions/{id}")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> deleteSession(@PathVariable Long id) {
        attendanceService.deleteSession(id);
        return ResponseEntity.ok(new ApiResponse("Attendance session deleted successfully", null));
    }

    // Attendance Record Management

    @PostMapping("/records")
    @PreAuthorize("hasAnyRole('HUYNH_TRUONG', 'DU_TRUONG', 'DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> createRecord(
            @Valid @RequestBody AttendanceRecordRequestDto dto,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        AttendanceRecordResponseDto response = attendanceService.createRecord(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Attendance record created successfully", response));
    }

    @GetMapping("/records/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getRecordById(@PathVariable Long id) {
        AttendanceRecordResponseDto response = attendanceService.getRecordById(id);
        return ResponseEntity.ok(new ApiResponse("Success", response));
    }

    @GetMapping("/records/session/{sessionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getRecordsBySession(@PathVariable Long sessionId) {
        List<AttendanceRecordResponseDto> records = attendanceService.getRecordsBySession(sessionId);
        return ResponseEntity.ok(new ApiResponse("Success", records));
    }

    @GetMapping("/records/enrollment/{enrollmentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getRecordsByEnrollment(@PathVariable Long enrollmentId) {
        List<AttendanceRecordResponseDto> records = attendanceService.getRecordsByEnrollment(enrollmentId);
        return ResponseEntity.ok(new ApiResponse("Success", records));
    }

    @PutMapping("/records/{id}")
    @PreAuthorize("hasAnyRole('HUYNH_TRUONG', 'DU_TRUONG', 'DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody AttendanceRecordRequestDto dto) {
        AttendanceRecordResponseDto response = attendanceService.updateRecord(id, dto);
        return ResponseEntity.ok(new ApiResponse("Attendance record updated successfully", response));
    }

    @DeleteMapping("/records/{id}")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> deleteRecord(@PathVariable Long id) {
        attendanceService.deleteRecord(id);
        return ResponseEntity.ok(new ApiResponse("Attendance record deleted successfully", null));
    }

    @GetMapping("/records/check")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> checkEnrollmentAttended(
            @RequestParam Long sessionId,
            @RequestParam Long enrollmentId) {
        boolean attended = attendanceService.isEnrollmentAttended(sessionId, enrollmentId);
        return ResponseEntity.ok(new ApiResponse("Success", attended));
    }

    // Batch Operations

    @PostMapping("/records/batch")
    @PreAuthorize("hasAnyRole('HUYNH_TRUONG', 'DU_TRUONG', 'DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> createBatchRecords(
            @Valid @RequestBody List<AttendanceRecordRequestDto> dtos,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        List<AttendanceRecordResponseDto> responses = attendanceService.createBatchRecords(dtos, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Batch attendance records created. Successful: " + responses.size(), responses));
    }

    @PostMapping("/sessions/auto-create")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> autoCreateSessions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AttendanceSessionResponseDto> sessions = attendanceService.autoCreateSessionsForDate(date);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Auto-created " + sessions.size() + " sessions", sessions));
    }

    // Statistics & Reports

    @GetMapping("/sessions/{id}/statistics")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getSessionWithStatistics(@PathVariable Long id) {
        AttendanceSessionResponseDto response = attendanceService.getSessionWithStatistics(id);
        return ResponseEntity.ok(new ApiResponse("Success", response));
    }

    @GetMapping("/sessions/classroom/{classroomId}/statistics")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getSessionsWithStatistics(
            @PathVariable Long classroomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<AttendanceSessionResponseDto> sessions = attendanceService.getSessionsWithStatisticsInDateRange(
                classroomId, startDate, endDate);
        return ResponseEntity.ok(new ApiResponse("Success", sessions));
    }

    @GetMapping("/reports/frequent-absent")
    @PreAuthorize("hasAnyRole('HUYNH_TRUONG', 'DU_TRUONG', 'DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> getFrequentAbsentStudents(
            @RequestParam Long classroomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "3") int threshold) {
        List<Long> enrollmentIds = attendanceService.getFrequentAbsentEnrollments(classroomId, startDate, endDate, threshold);
        return ResponseEntity.ok(new ApiResponse("Found " + enrollmentIds.size() + " enrollments", enrollmentIds));
    }

    @GetMapping("/reports/attendance-rate")
    @PreAuthorize("hasAnyRole('HUYNH_TRUONG', 'DU_TRUONG', 'DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> getAttendanceRate(
            @RequestParam Long classroomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Double rate = attendanceService.getClassroomAttendanceRate(classroomId, startDate, endDate);
        return ResponseEntity.ok(new ApiResponse("Attendance rate: " + String.format("%.2f%%", rate), rate));
    }

    // Helper Methods

    private Long getUserIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractUserId(token);
        }
        throw new IllegalStateException("JWT token not found in request");
    }
}
