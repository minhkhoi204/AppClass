package com.programming.common.common_dto.classroom;

import lombok.Data;

import java.util.Set;

@Data
public class ClassroomRequestDto {
    private String name;
    private String level;
    private String academicYear;
    private String room;
    private String schedule;
    private String note;
    private Set<Long> studentIds;
    private Set<Long> catechistIds;
}
