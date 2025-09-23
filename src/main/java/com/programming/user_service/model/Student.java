package com.programming.user_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    private Long userId; // Dùng chung ID với User

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String fatherName;
    private String fatherPhoneNum;
    private String motherName;
    private String motherPhoneNum;
    private String address;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;
}