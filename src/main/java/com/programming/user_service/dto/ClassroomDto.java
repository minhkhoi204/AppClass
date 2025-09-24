package com.programming.user_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClassroomDto {
    private Long id;
    private String name;

    // trả danh sách student của lớp
    private List<StudentDto> students;
}
