package com.programming.user_service.dto;

import lombok.Data;

@Data
public class StudentDto {
    private Long userId;

    private String fullName;
    private String saintName;
    private String email;
    private String phone;

    private String fatherName;
    private String fatherPhoneNum;
    private String motherName;
    private String motherPhoneNum;
    private String address;

    private String classroomName; // thay vì classJoined
}
