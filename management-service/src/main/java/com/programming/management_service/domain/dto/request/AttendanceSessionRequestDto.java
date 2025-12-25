package com.programming.management_service.domain.dto.request;

import com.programming.management_service.domain.model.SessionStatus;
import com.programming.management_service.domain.model.SessionType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceSessionRequestDto {

    @NotNull(message = "Classroom ID is required")
    private Long classroomId;

    @NotNull(message = "Session date is required")
    private LocalDate sessionDate;

    @NotNull(message = "Session type is required")
    private SessionType sessionType;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private SessionStatus status;
    
    private String note;
}
