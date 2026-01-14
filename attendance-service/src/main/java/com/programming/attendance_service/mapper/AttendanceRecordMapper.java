package com.programming.attendance_service.mapper;

import com.programming.attendance_service.domain.dto.request.AttendanceRecordRequestDto;
import com.programming.attendance_service.domain.dto.response.AttendanceRecordResponseDto;
import com.programming.attendance_service.domain.model.AttendanceRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AttendanceRecordMapper {

    // transform AttendanceRecordRequestDto to AttendanceRecord entity, recordedBy: id catechist
    public AttendanceRecord toAttendanceRecordEntity(AttendanceRecordRequestDto dto, Long recordedBy) {
        return AttendanceRecord.builder()
                .attendanceSessionId(dto.getAttendanceSessionId())
                .studentId(dto.getStudentId())
                .status(dto.getStatus())
                .checkedInTime(dto.getCheckedInTime() != null ? dto.getCheckedInTime() : LocalDateTime.now())
                .note(dto.getNote())
                .recordedBy(recordedBy)
                .build();
    }

    // transform AttendanceRecord entity to AttendanceRecordResponseDto
    public AttendanceRecordResponseDto toAttendanceRecordResponseDto(AttendanceRecord record) {
        return AttendanceRecordResponseDto.builder()
                .id(record.getId())
                .attendanceSessionId(record.getAttendanceSessionId())
                .studentId(record.getStudentId())
                .status(record.getStatus())
                .checkedInTime(record.getCheckedInTime())
                .note(record.getNote())
                .recordedBy(record.getRecordedBy())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }

    // update AttendanceRecord entity from request DTO
    public void updateAttendanceRecordFromDto(AttendanceRecord record, AttendanceRecordRequestDto dto) {
        if (dto.getStatus() != null) {
            record.setStatus(dto.getStatus());
        }
        if (dto.getCheckedInTime() != null) {
            record.setCheckedInTime(dto.getCheckedInTime());
        }
        if (dto.getNote() != null) {
            record.setNote(dto.getNote());
        }
    }
}
