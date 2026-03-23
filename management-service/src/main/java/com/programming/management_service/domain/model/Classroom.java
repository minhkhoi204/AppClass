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
    
    private String academicYear; // "2024", "2025"
    
    private String room; 
    
    //private Integer maxStudents; // Maximum number of students allowed
    
    private String schedule; // "Sunday 7:00 AM - 9:00 AM"
    
    private String note;
    
    // place order in the grade system
    @Column(name = "class_order")
    private Integer classOrder;
    
    @Column(name = "next_classroom_name", length = 100)
    private String nextClassroomName;

    // check if final class in system
    @Column(name = "is_final_class")
    private Boolean isFinalClass = false;

}
