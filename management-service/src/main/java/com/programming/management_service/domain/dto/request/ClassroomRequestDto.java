package com.programming.management_service.domain.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class ClassroomRequestDto {
    private String name;
    private String level;
    private String academicYear;
    private String room;
    //private Integer maxStudents;
    private String schedule;
    private String note;

    private Set<Long> studentIds;
    private Set<Long> catechistIds;
}
