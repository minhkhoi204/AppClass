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
@Table(name = "enrollment",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"classroom_id", "academic_year", "student_code"}),
           @UniqueConstraint(columnNames = {"student_id", "classroom_id", "academic_year"})
       })
public class Enrollment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "student_id", nullable = false)
    private Long studentId;
    
    @Column(name = "classroom_id", nullable = false)
    private Long classroomId;
    
    // can be reused
    @Column(name = "student_code", nullable = false, length = 20)
    private String studentCode;
    
    @Column(name = "academic_year", nullable = false, length = 20)
    private String academicYear;
    
    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status;
    
    @Column(length = 500)
    private String note;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
