package com.programming.user_service.domain.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequestDto {

    private String fullName;
    private String saintName;
    private LocalDate dateOfBirth;

    private String fatherName;
    private String fatherPhoneNum;
    private String motherName;
    private String motherPhoneNum;

    private String address;

    private Long userId;
}