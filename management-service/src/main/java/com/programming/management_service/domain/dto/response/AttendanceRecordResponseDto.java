package com.programming.management_service.domain.dto.response;

import com.programming.management_service.domain.model.AttendanceStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceRecordResponseDto {
    
    private Long id;
    
    private Long attendanceSessionId;
    
    private Long enrollmentId;
    
    private String studentCode; // For display (e.g., "01vd1")
    
    private AttendanceStatus status;
    
    private LocalDateTime checkedInTime;
    
    private String note;
    
    private Long recordedBy;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private String studentName; // adjust

    private String recordedByName; // adjust
}
