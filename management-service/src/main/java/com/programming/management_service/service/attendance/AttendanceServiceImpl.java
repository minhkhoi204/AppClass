package com.programming.management_service.service.attendance;

import com.programming.common.exception.AlreadyExistsException;
import com.programming.common.exception.ResourceNotFoundException;
import com.programming.management_service.domain.dto.request.AttendanceRecordRequestDto;
import com.programming.management_service.domain.dto.request.AttendanceSessionRequestDto;
import com.programming.management_service.domain.dto.response.AttendanceRecordResponseDto;
import com.programming.management_service.domain.dto.response.AttendanceSessionResponseDto;
import com.programming.management_service.domain.model.*;
import com.programming.management_service.mapper.AttendanceRecordMapper;
import com.programming.management_service.mapper.AttendanceSessionMapper;
import com.programming.management_service.repository.AttendanceRecordRepository;
import com.programming.management_service.repository.AttendanceSessionRepository;
import com.programming.management_service.repository.ClassroomRepository;
import com.programming.management_service.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceSessionRepository sessionRepository;
    private final AttendanceRecordRepository recordRepository;
    private final ClassroomRepository classroomRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceSessionMapper sessionMapper;
    private final AttendanceRecordMapper recordMapper;

    // Session Management

    @Override
    @Transactional
    public AttendanceSessionResponseDto createSession(AttendanceSessionRequestDto dto, Long createdBy) {
        // Validate classroom exists
        if (!classroomRepository.existsById(dto.getClassroomId())) {
            throw new ResourceNotFoundException("Classroom not found with id: " + dto.getClassroomId());
        }

        // Check if session already exists
        if (sessionRepository.existsByClassroomIdAndSessionDateAndSessionType(
                dto.getClassroomId(), dto.getSessionDate(), dto.getSessionType())) {
            throw new AlreadyExistsException("Attendance session already exists for this classroom, date and type");
        }

        AttendanceSession session = sessionMapper.toAttendanceSessionEntity(dto, createdBy);
        AttendanceSession saved = sessionRepository.save(session);
        
        log.info("Created attendance session: {} for classroom: {}", saved.getId(), dto.getClassroomId());
        return sessionMapper.toAttendanceSessionResponseDto(saved);
    }

    @Override
    public AttendanceSessionResponseDto getSessionById(Long id) {
        AttendanceSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance session not found with id: " + id));
        return sessionMapper.toAttendanceSessionResponseDto(session);
    }

    @Override
    public List<AttendanceSessionResponseDto> getSessionsByClassroom(Long classroomId) {
        List<AttendanceSession> sessions = sessionRepository.findByClassroomId(classroomId);
        return sessions.stream()
                .map(sessionMapper::toAttendanceSessionResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceSessionResponseDto> getSessionsByClassroomAndDate(Long classroomId, LocalDate sessionDate) {
        List<AttendanceSession> sessions = sessionRepository.findByClassroomIdAndSessionDate(classroomId, sessionDate);
        return sessions.stream()
                .map(sessionMapper::toAttendanceSessionResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AttendanceSessionResponseDto updateSession(Long id, AttendanceSessionRequestDto dto) {
        AttendanceSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance session not found with id: " + id));

        sessionMapper.updateAttendanceSessionFromDto(session, dto);
        AttendanceSession updated = sessionRepository.save(session);
        
        log.info("Updated attendance session: {}", id);
        return sessionMapper.toAttendanceSessionResponseDto(updated);
    }

    @Override
    @Transactional
    public AttendanceSessionResponseDto closeSession(Long id) {
        AttendanceSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance session not found with id: " + id));

        session.setStatus(SessionStatus.CLOSED);
        AttendanceSession updated = sessionRepository.save(session);
        
        log.info("Closed attendance session: {}", id);
        return sessionMapper.toAttendanceSessionResponseDto(updated);
    }

    @Override
    @Transactional
    public AttendanceSessionResponseDto cancelSession(Long id) {
        AttendanceSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance session not found with id: " + id));

        session.setStatus(SessionStatus.CANCELLED);
        AttendanceSession updated = sessionRepository.save(session);
        
        log.info("Cancelled attendance session: {}", id);
        return sessionMapper.toAttendanceSessionResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteSession(Long id) {
        if (!sessionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Attendance session not found with id: " + id);
        }

        // Delete all related records first
        recordRepository.deleteByAttendanceSessionId(id);
        sessionRepository.deleteById(id);
        
        log.info("Deleted attendance session: {} and all related records", id);
    }

    // Attendance Record Management

    @Override
    @Transactional
    public AttendanceRecordResponseDto createRecord(AttendanceRecordRequestDto dto, Long recordedBy) {
        // Validate session exists and is open
        AttendanceSession session = sessionRepository.findById(dto.getAttendanceSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Attendance session not found with id: " + dto.getAttendanceSessionId()));

        if (session.getStatus() == SessionStatus.CLOSED) {
            throw new IllegalStateException("Cannot create attendance record. Session is closed.");
        }

        if (session.getStatus() == SessionStatus.CANCELLED) {
            throw new IllegalStateException("Cannot create attendance record. Session is cancelled.");
        }

        // Get classroom info to find enrollment
        Classroom classroom = classroomRepository.findById(session.getClassroomId())
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

        // Resolve enrollmentId from studentCode
        Enrollment enrollment = enrollmentRepository
                .findByClassroomIdAndAcademicYearAndStudentCodeAndStatus(
                        classroom.getId(), 
                        classroom.getAcademicYear(), 
                        dto.getStudentCode(),
                        EnrollmentStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No active enrollment found for student code: " + dto.getStudentCode() + 
                        " in classroom: " + classroom.getName()));

        // Check if enrollment already attended
        if (recordRepository.existsByAttendanceSessionIdAndEnrollmentId(
                dto.getAttendanceSessionId(), enrollment.getId())) {
            throw new AlreadyExistsException("Student already has attendance record in this session");
        }

        AttendanceRecord record = recordMapper.toAttendanceRecordEntity(dto, enrollment.getId(), recordedBy);
        AttendanceRecord saved = recordRepository.save(record);
        
        log.info("Created attendance record for student code: {} (enrollment: {}) in session: {}", 
                dto.getStudentCode(), enrollment.getId(), dto.getAttendanceSessionId());
        
        AttendanceRecordResponseDto response = recordMapper.toAttendanceRecordResponseDto(saved);
        response.setStudentCode(enrollment.getStudentCode()); // enrich with student code
        return response;
    }

    @Override
    public AttendanceRecordResponseDto getRecordById(Long id) {
        AttendanceRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with id: " + id));
        return enrichWithStudentCode(record);
    }

    @Override
    public List<AttendanceRecordResponseDto> getRecordsBySession(Long sessionId) {
        List<AttendanceRecord> records = recordRepository.findByAttendanceSessionId(sessionId);
        return records.stream()
                .map(this::enrichWithStudentCode)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceRecordResponseDto> getRecordsByEnrollment(Long enrollmentId) {
        List<AttendanceRecord> records = recordRepository.findByEnrollmentId(enrollmentId);
        return records.stream()
                .map(this::enrichWithStudentCode)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AttendanceRecordResponseDto updateRecord(Long id, AttendanceRecordRequestDto dto) {
        AttendanceRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with id: " + id));

        // Validate session is still open
        AttendanceSession session = sessionRepository.findById(record.getAttendanceSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Attendance session not found"));

        if (session.getStatus() == SessionStatus.CLOSED) {
            throw new IllegalStateException("Cannot update attendance record. Session is closed.");
        }

        recordMapper.updateAttendanceRecordFromDto(record, dto);
        AttendanceRecord updated = recordRepository.save(record);
        
        log.info("Updated attendance record: {}", id);
        return enrichWithStudentCode(updated);
    }

    @Override
    @Transactional
    public void deleteRecord(Long id) {
        if (!recordRepository.existsById(id)) {
            throw new ResourceNotFoundException("Attendance record not found with id: " + id);
        }

        recordRepository.deleteById(id);
        log.info("Deleted attendance record: {}", id);
    }

    @Override
    public boolean isEnrollmentAttended(Long sessionId, Long enrollmentId) {
        return recordRepository.existsByAttendanceSessionIdAndEnrollmentId(sessionId, enrollmentId);
    }

    // Batch Operations

    @Override
    @Transactional
    public List<AttendanceRecordResponseDto> createBatchRecords(List<AttendanceRecordRequestDto> dtos, Long recordedBy) {
        return dtos.stream()
                .map(dto -> {
                    try {
                        return createRecord(dto, recordedBy);
                    } catch (Exception e) {
                        log.error("Failed to create attendance record for student code: {}", dto.getStudentCode(), e);
                        return null;
                    }
                })
                .filter(record -> record != null)
                .collect(Collectors.toList());
    }


    // test for attendance session auto creation: test 1
    @Override
    @Transactional
    public List<AttendanceSessionResponseDto> autoCreateSessionsForDate(LocalDate date) {
        List<Classroom> classrooms = classroomRepository.findAll();
        
        return classrooms.stream()
                .flatMap(classroom -> {
                    // Create MASS_ATTENDANCE session (6:45-7:00)
                    AttendanceSessionRequestDto massDto = AttendanceSessionRequestDto.builder()
                            .classroomId(classroom.getId())
                            .sessionDate(date)
                            .sessionType(SessionType.MASS_ATTENDANCE)
                            .startTime(date.atTime(6, 45))
                            .endTime(date.atTime(7, 0))
                            .status(SessionStatus.OPEN)
                            .note("Mass attendance")
                            .build();

                    // Create BEFORE_CLASS session (8:45-9:00)
                    AttendanceSessionRequestDto beforeDto = AttendanceSessionRequestDto.builder()
                            .classroomId(classroom.getId())
                            .sessionDate(date)
                            .sessionType(SessionType.BEFORE_CLASS)
                            .startTime(date.atTime(8, 45))
                            .endTime(date.atTime(9, 0))
                            .status(SessionStatus.OPEN)
                            .note("Before class attendance")
                            .build();

                    // Create AFTER_CLASS session 10:00-10:15)
                    AttendanceSessionRequestDto afterDto = AttendanceSessionRequestDto.builder()
                            .classroomId(classroom.getId())
                            .sessionDate(date)
                            .sessionType(SessionType.AFTER_CLASS)
                            .startTime(date.atTime(10, 0))
                            .endTime(date.atTime(10, 15))
                            .status(SessionStatus.OPEN)
                            .note("After class attendance")
                            .build();

                    return List.of(massDto, beforeDto, afterDto).stream()
                            .map(dto -> {
                                try {
                                    return createSession(dto, 1L); // System user
                                } catch (Exception e) {
                                    log.error("Failed to create session for classroom: {}", classroom.getId(), e);
                                    return null;
                                }
                            })
                            .filter(session -> session != null);
                })
                .collect(Collectors.toList());
    }

    // Statistics & Reports

    @Override
    public AttendanceSessionResponseDto getSessionWithStatistics(Long sessionId) {
        AttendanceSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance session not found with id: " + sessionId));

        AttendanceSessionResponseDto dto = sessionMapper.toAttendanceSessionResponseDto(session);
        
        // Calculate statistics
        long totalRecords = recordRepository.countByAttendanceSessionId(sessionId);
        long presentCount = recordRepository.countBySessionIdAndStatus(sessionId, AttendanceStatus.PRESENT);
        
        dto.setTotalRecords((int) totalRecords);
        dto.setPresentCount((int) presentCount);
        
        return dto;
    }

    @Override
    public List<AttendanceSessionResponseDto> getSessionsWithStatisticsInDateRange(
            Long classroomId, LocalDate startDate, LocalDate endDate) {
        
        List<AttendanceSession> sessions = sessionRepository.findByClassroomIdAndDateRange(
                classroomId, startDate, endDate);
        
        return sessions.stream()
                .map(session -> {
                    AttendanceSessionResponseDto dto = sessionMapper.toAttendanceSessionResponseDto(session);
                    long totalRecords = recordRepository.countByAttendanceSessionId(session.getId());
                    long presentCount = recordRepository.countBySessionIdAndStatus(session.getId(), AttendanceStatus.PRESENT);
                    dto.setTotalRecords((int) totalRecords);
                    dto.setPresentCount((int) presentCount);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceRecordResponseDto getStudentAttendanceReport(
            Long studentId, LocalDate startDate, LocalDate endDate) {
        // need more complex logic to aggregate data
        throw new UnsupportedOperationException("This feature is not yet implemented");
    }

    @Override
    public List<Long> getFrequentAbsentEnrollments(
            Long classroomId, LocalDate startDate, LocalDate endDate, int threshold) {
        
        List<AttendanceSession> sessions = sessionRepository.findByClassroomIdAndDateRange(
                classroomId, startDate, endDate);
        
        List<Long> sessionIds = sessions.stream()
                .map(AttendanceSession::getId)
                .collect(Collectors.toList());
        
        // Get all records for these sessions
        return sessionIds.stream()
                .flatMap(sessionId -> recordRepository.findByAttendanceSessionIdAndStatus(sessionId, AttendanceStatus.ABSENT).stream())
                .collect(Collectors.groupingBy(AttendanceRecord::getEnrollmentId, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() >= threshold)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
    }

    @Override
    public Double getClassroomAttendanceRate(Long classroomId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceSession> sessions = sessionRepository.findByClassroomIdAndDateRange(
                classroomId, startDate, endDate);
        
        long totalRecords = 0;
        long presentRecords = 0;
        
        for (AttendanceSession session : sessions) {
            totalRecords += recordRepository.countByAttendanceSessionId(session.getId());
            presentRecords += recordRepository.countBySessionIdAndStatus(session.getId(), AttendanceStatus.PRESENT);
        }
        
        if (totalRecords == 0) {
            return 0.0;
        }
        
        return (double) presentRecords / totalRecords * 100;
    }
    
    // enrich attendance record response with student code
    private AttendanceRecordResponseDto enrichWithStudentCode(AttendanceRecord record) {
        AttendanceRecordResponseDto dto = recordMapper.toAttendanceRecordResponseDto(record);
        
        //enrollment to get student code
        enrollmentRepository.findById(record.getEnrollmentId())
                .ifPresent(enrollment -> dto.setStudentCode(enrollment.getStudentCode()));
        
        return dto;
    }
}
