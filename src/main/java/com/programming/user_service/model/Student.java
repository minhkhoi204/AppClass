package com.programming.user_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Student có id riêng

    private String saintName;
    private String fullName;

    private LocalDate dateOfBirth;

    // Thông tin phụ huynh
    private String fatherName;
    private String fatherPhoneNum;
    private String motherName;
    private String motherPhoneNum;
    private String address;

    // Liên kết với lớp học
    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    // Liên kết với tài khoản User (nếu có)
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id_user")
    private User user;
}