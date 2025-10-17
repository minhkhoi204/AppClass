package com.programming.common_dto.student;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponseDto {

    private Long id;

    private String fullName;
    private String saintName;
    private LocalDate dateOfBirth;

    private String fatherName;
    private String fatherPhoneNum;
    private String motherName;
    private String motherPhoneNum;

    private String address;

    private Long userId;

    private String classroomName; // assuming Student has a classroom field
    private Long classroomId;
}