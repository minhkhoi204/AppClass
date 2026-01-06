package com.programming.management_service.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "attendance_record", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"attendance_session_id", "student_code"}))
public class AttendanceRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // attendance session ID
    @Column(name = "attendance_session_id", nullable = false)
    private Long attendanceSessionId;
    
    // student code (e.g., "01vd1", "02kt2")
    @Column(name = "student_code", nullable = false, length = 20)
    private String studentCode;

    // PRESENT, ABSENT, LATE, EXCUSED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;
    
    private LocalDateTime checkedInTime;
    
    @Column(length = 500)
    private String note;
    
    // id of the user who recorded the attendance
    @Column(nullable = false)
    private Long recordedBy;
    
    // time created of the record
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // time updated of the record
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
