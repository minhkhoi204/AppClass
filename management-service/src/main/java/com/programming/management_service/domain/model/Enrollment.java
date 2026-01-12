package com.programming.management_service.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollment", 
       indexes = {
           @Index(name = "idx_student_classroom_year", 
                  columnList = "student_id, classroom_id, academic_year"),
           @Index(name = "idx_student_status", 
                  columnList = "student_id, status"),
           @Index(name = "idx_classroom_year_status", 
                  columnList = "classroom_id, academic_year, status"),
           @Index(name = "idx_student_code", 
                  columnList = "student_code")
       },
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_student_classroom_year",
                           columnNames = {"student_id", "classroom_id", "academic_year"}),
           @UniqueConstraint(name = "uk_student_code_year",
                           columnNames = {"student_code", "academic_year"})
       })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Enrollment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "classroom_id", nullable = false)
    private Long classroomId;
    
    @Column(name = "student_code", nullable = false, length = 20)
    private String studentCode;

    @Column(name = "academic_year", nullable = false, length = 20)
    private String academicYear;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;
    
    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

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
