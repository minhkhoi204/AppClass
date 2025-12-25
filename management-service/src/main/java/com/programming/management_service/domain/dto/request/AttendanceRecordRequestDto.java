package com.programming.management_service.domain.dto.request;

import com.programming.management_service.domain.model.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceRecordRequestDto {
    
    @NotNull(message = "Attendance session ID is required")
    private Long attendanceSessionId;
    
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    @NotNull(message = "Attendance status is required")
    private AttendanceStatus status;
    
    private LocalDateTime checkedInTime;
 
    private String note;
}
