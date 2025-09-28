package com.programming.management_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClassroomDto {
    private Long id;
    private String name;

    // list of students
    private List<StudentDto> students;
}
