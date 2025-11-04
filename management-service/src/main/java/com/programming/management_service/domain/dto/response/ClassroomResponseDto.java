package com.programming.management_service.domain.dto.response;

import com.programming.common.common_dto.student.StudentResponseDto;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ClassroomResponseDto {
    private Long id;
    private String name;
    private Set<Long> studentIds;
    private List<StudentResponseDto> students;
}