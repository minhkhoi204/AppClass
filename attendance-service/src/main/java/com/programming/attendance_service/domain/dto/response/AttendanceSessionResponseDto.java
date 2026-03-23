package com.programming.attendance_service.domain.dto.response;

import com.programming.attendance_service.domain.model.SessionStatus;
import com.programming.attendance_service.domain.model.SessionType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceSessionResponseDto {
    
    private Long id;
    
    private Long classroomId;
    
    private LocalDate sessionDate;
    
    private SessionType sessionType;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private SessionStatus status;
    
    private Long createdBy;
    
    private String note;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Integer totalRecords;

    private Integer presentCount;
}
