package com.programming.management_service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentDto {
    private Long id;
    private String fullName;
    private String saintName;
    private LocalDate dateOfBirth;

    // parents info
    private String fatherName;
    private String fatherPhoneNum;
    private String motherName;
    private String motherPhoneNum;
    private String address;

    private String classroomName; // thay vì classJoined

    private Long userId; // lấy từ User nếu đã link
}
