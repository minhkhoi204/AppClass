package com.programming.management_service.mapper;

import com.programming.management_service.domain.dto.request.ClassroomRequestDto;
import com.programming.management_service.domain.dto.response.ClassroomResponseDto;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.programming.management_service.domain.dto.ClassroomDto;
import com.programming.management_service.domain.model.Classroom;

import java.util.HashSet;


@Component
@RequiredArgsConstructor
public class ClassroomMapper {

    public Classroom toClassroomEntity(ClassroomRequestDto dto) {
        Classroom c = new Classroom();
        c.setName(dto.getName());
        c.setStudentIds(dto.getStudentIds() != null ? new HashSet<>(dto.getStudentIds()) : new HashSet<>());
        return c;
    }

    public ClassroomResponseDto toClassroomResponseDto(Classroom classroom) {
        ClassroomResponseDto dto = new ClassroomResponseDto();
        dto.setId(classroom.getId());
        dto.setName(classroom.getName());
        dto.setStudentIds(classroom.getStudentIds());
        return dto;
    }

}