package com.programming.management_service.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    private String level; //"pink, green, blue, yellow, brown"
    
    private Integer academicYear; // 2024, 2025
    
    private String room; 
    
    //private Integer maxStudents; // Maximum number of students allowed
    
    private String schedule; // "Sunday 7:00 AM - 9:00 AM"
    
    private String note;

    @ElementCollection
    private Set<Long> studentIds = new HashSet<>();
    
    @ElementCollection
    private Set<Long> catechistIds = new HashSet<>(); 
}
