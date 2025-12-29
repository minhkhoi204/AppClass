package com.programming.management_service.mapper;

import com.programming.management_service.domain.dto.request.AttendanceSessionRequestDto;
import com.programming.management_service.domain.dto.response.AttendanceSessionResponseDto;
import com.programming.management_service.domain.model.AttendanceSession;
import com.programming.management_service.domain.model.SessionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttendanceSessionMapper {

    // transform AttendanceSessionRequestDto to AttendanceSession entity
    public AttendanceSession toAttendanceSessionEntity(AttendanceSessionRequestDto dto, Long createdBy) {
        return AttendanceSession.builder()
                .classroomId(dto.getClassroomId())
                .sessionDate(dto.getSessionDate())
                .sessionType(dto.getSessionType())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .status(dto.getStatus() != null ? dto.getStatus() : SessionStatus.OPEN)
                .createdBy(createdBy)
                .note(dto.getNote())
                .build();
    }

    // transform AttendanceSession entity to AttendanceSessionResponseDto
    public AttendanceSessionResponseDto toAttendanceSessionResponseDto(AttendanceSession session) {
        return AttendanceSessionResponseDto.builder()
                .id(session.getId())
                .classroomId(session.getClassroomId())
                .sessionDate(session.getSessionDate())
                .sessionType(session.getSessionType())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .status(session.getStatus())
                .createdBy(session.getCreatedBy())
                .note(session.getNote())
                .createdAt(session.getCreatedAt())
                .updatedAt(session.getUpdatedAt())
                .build();
    }

    // update AttendanceSession entity from request DTO
    public void updateAttendanceSessionFromDto(AttendanceSession session, AttendanceSessionRequestDto dto) {
        if (dto.getSessionDate() != null) {
            session.setSessionDate(dto.getSessionDate());
        }
        if (dto.getSessionType() != null) {
            session.setSessionType(dto.getSessionType());
        }
        if (dto.getStartTime() != null) {
            session.setStartTime(dto.getStartTime());
        }
        if (dto.getEndTime() != null) {
            session.setEndTime(dto.getEndTime());
        }
        if (dto.getStatus() != null) {
            session.setStatus(dto.getStatus());
        }
        if (dto.getNote() != null) {
            session.setNote(dto.getNote());
        }
    }
}
