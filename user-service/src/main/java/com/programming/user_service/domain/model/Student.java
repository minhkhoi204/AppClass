package com.programming.user_service.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "students")
@Setter
@Getter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private String fatherName;
    private String fatherPhoneNum;
    private String motherName;
    private String motherPhoneNum;
    private String address;

    private Long classroomId; //id classroom student attended
    
    @Column(unique = true)
    private String studentCode; // student code
}

