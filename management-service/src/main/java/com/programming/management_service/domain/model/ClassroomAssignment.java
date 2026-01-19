package com.programming.management_service.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.programming.management_service.domain.enums.AssignmentStatus;

@Entity
@Table(name = "classroom_assignment",
       indexes = {
           @Index(name = "idx_catechist_classroom_year",
                  columnList = "catechist_id, classroom_id, academic_year"),
           @Index(name = "idx_catechist_status",
                  columnList = "catechist_id, status"),
           @Index(name = "idx_classroom_year_status",
                  columnList = "classroom_id, academic_year, status")
       },
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_catechist_classroom_year",
                           columnNames = {"catechist_id", "classroom_id", "academic_year"})
       })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassroomAssignment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "catechist_id", nullable = false)
    private Long catechistId;
    
    @Column(name = "classroom_id", nullable = false)
    private Long classroomId;
    
    @Column(name = "academic_year", nullable = false, length = 20)
    private String academicYear;  // "2026-2027"
    

    @Column(length = 50)
    private String role;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AssignmentStatus status = AssignmentStatus.ACTIVE;
    
    @Column(name = "assigned_date")
    private LocalDate assignedDate;
    
    @Column(name = "completion_date")
    private LocalDate completionDate;
    
    @Column(length = 500)
    private String note;
    
    @Column(name = "created_by")
    private Long createdBy;
    
    @Column(name = "updated_by")
    private Long updatedBy;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
