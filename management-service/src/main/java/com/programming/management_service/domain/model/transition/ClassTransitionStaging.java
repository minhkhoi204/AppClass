package com.programming.management_service.domain.model.transition;

import com.programming.management_service.domain.model.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "class_transition_staging",
       indexes = {
           @Index(name = "idx_student_old_year",
                  columnList = "student_id, old_academic_year"),
           @Index(name = "idx_student_new_year",
                  columnList = "student_id, new_academic_year"),
           @Index(name = "idx_new_classroom_year",
                  columnList = "new_classroom_id, new_academic_year"),
           @Index(name = "idx_finalized",
                  columnList = "is_finalized")
       },
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_student_new_year",
                           columnNames = {"student_id", "new_academic_year"})
       })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassTransitionStaging {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "student_id", nullable = false)
    private Long studentId;
    
    @Column(name = "old_classroom_id", nullable = false)
    private Long oldClassroomId;
    
    @Column(name = "old_academic_year", nullable = false, length = 20)
    private String oldAcademicYear; // "2025-2026"
    
    @Column(name = "old_student_code", length = 20)
    private String oldStudentCode; // "25-TS-1-001"
    
    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", nullable = false)
    private EnrollmentStatus oldStatus;
    
    // new year cls
    @Column(name = "new_classroom_id")
    private Long newClassroomId;
    
    @Column(name = "new_academic_year", nullable = false, length = 20)
    private String newAcademicYear; // "2026-2027"
    
    @Column(name = "new_student_code", length = 20)
    private String newStudentCode;
    
    @Column(length = 500)
    private String note;
    
    @Column(name = "assigned_by")
    private Long assignedBy;
    
    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;
    
    @Column(name = "is_finalized", nullable = false)
    @Builder.Default
    private Boolean isFinalized = false;
    
    @Column(name = "finalized_by")
    private Long finalizedBy;
    
    @Column(name = "finalized_at")
    private LocalDateTime finalizedAt;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
