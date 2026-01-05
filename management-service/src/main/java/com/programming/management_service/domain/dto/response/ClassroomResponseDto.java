package com.programming.management_service.domain.dto.response;

import com.programming.common.common_dto.student.StudentResponseDto;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ClassroomResponseDto {
    private Long id;
    private String name;
    private String level;
    private String academicYear;
    private String room;
    //private Integer maxStudents;
    private String schedule;
    private String note;

    private Set<Long> studentIds;
    private Set<Long> catechistIds;
    private List<StudentResponseDto> students;
}