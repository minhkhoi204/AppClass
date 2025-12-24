package com.programming.management_service.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "attendance_session")
public class AttendanceSession {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    

    @Column(nullable = false)
    private Long classroomId;
    
    // date of the session (sunday)
    @Column(nullable = false)
    private LocalDate sessionDate;
    
    // session before or after class
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionType sessionType;
    
    // time started
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    // time ended
    private LocalDateTime endTime;
    
    //sessionStatus
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private SessionStatus status = SessionStatus.OPEN;
    
    // id of the user who created the session
    @Column(nullable = false)
    private Long createdBy;
    
    // note
    @Column(length = 500)
    private String note;
    
    // time created of the record
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // time updated of the record
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
