package com.programming.management_service.model;

import com.programming.management_service.dto.UserDto;
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
    private Long id;

    private String saintName;
    private String fullName;
    private LocalDate dateOfBirth;

    private String fatherName;
    private String fatherPhoneNum;
    private String motherName;
    private String motherPhoneNum;
    private String address;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    private Long userId; // không ánh xạ sang entity User

    @Transient
    private UserDto user; // set thủ công nếu cần từ Feign
}