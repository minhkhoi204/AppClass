package com.programming.management_service.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

// staging table for transition plan
@Entity
@Table(name = "transition_staging",
       indexes = {
           @Index(name = "idx_student_finalized", 
                  columnList = "student_id, is_finalized"),
           @Index(name = "idx_old_classroom_year", 
                  columnList = "old_classroom_id, old_academic_year"),
           @Index(name = "idx_new_classroom_year", 
                  columnList = "new_classroom_id, new_academic_year")
       })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransitionStaging {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // old enrollment info
    @Column(name = "student_id", nullable = false)
    private Long studentId;
    
    @Column(name = "old_classroom_id", nullable = false)
    private Long oldClassroomId;
    
    @Column(name = "old_academic_year", nullable = false, length = 20)
    private String oldAcademicYear;
    
    @Column(name = "old_student_code", length = 20)
    private String oldStudentCode;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", nullable = false)
    private EnrollmentStatus oldStatus; // COMPLETED or RETAINED
    
    // new enrollment info
    @Column(name = "new_classroom_id", nullable = false)
    private Long newClassroomId;
    
    @Column(name = "new_academic_year", nullable = false, length = 20)
    private String newAcademicYear;

    // generate after finalize, null when staging
    @Column(name = "new_student_code", length = 20)
    private String newStudentCode;
    
    @Column(name = "new_enrollment_id")
    private Long newEnrollmentId;
    
    // metadata
    @Column(length = 500)
    private String note;
    
    /*
    false = staging (reviewing)
    true = finalized (cant be adjusted)
    */
    @Column(name = "is_finalized", nullable = false)
    @Builder.Default
    private Boolean isFinalized = false;
    
    @Column(name = "created_by")
    private Long createdBy;
    
    @Column(name = "finalized_by")
    private Long finalizedBy;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "finalized_at")
    private LocalDateTime finalizedAt;
}
