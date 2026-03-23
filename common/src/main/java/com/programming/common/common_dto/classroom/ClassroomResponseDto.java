package com.programming.common.common_dto.classroom;

import com.programming.common.common_dto.student.StudentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomResponseDto {
    private Long id;
    private String name;
    private String level;
    private String academicYear;
    private String room;
    private String schedule;
    private String note;
    private Set<Long> studentIds;
    private Set<Long> catechistIds;
    private List<StudentResponseDto> students;
}
