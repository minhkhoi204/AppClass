package com.programming.management_service.mapper;

import com.programming.management_service.domain.dto.request.ClassroomRequestDto;
import com.programming.management_service.domain.dto.response.ClassroomResponseDto;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.programming.management_service.domain.model.Classroom;

import java.util.HashSet;


@Component
@RequiredArgsConstructor
public class ClassroomMapper {

    public Classroom toClassroomEntity(ClassroomRequestDto dto) {
        Classroom c = new Classroom();
        c.setName(dto.getName());
        c.setLevel(dto.getLevel());
        c.setAcademicYear(dto.getAcademicYear());
        c.setRoom(dto.getRoom());
        //c.setMaxStudents(dto.getMaxStudents());
        c.setSchedule(dto.getSchedule());
        c.setNote(dto.getNote());
        c.setStudentIds(dto.getStudentIds() != null ? new HashSet<>(dto.getStudentIds()) : new HashSet<>());
        c.setCatechistIds(dto.getCatechistIds() != null ? new HashSet<>(dto.getCatechistIds()) : new HashSet<>());
        return c;
    }

    public ClassroomResponseDto toClassroomResponseDto(Classroom classroom) {
        ClassroomResponseDto dto = new ClassroomResponseDto();
        dto.setId(classroom.getId());
        dto.setName(classroom.getName());
        dto.setLevel(classroom.getLevel());
        dto.setAcademicYear(classroom.getAcademicYear());
        dto.setRoom(classroom.getRoom());
        //dto.setMaxStudents(classroom.getMaxStudents());
        dto.setSchedule(classroom.getSchedule());
        dto.setNote(classroom.getNote());
        dto.setStudentIds(classroom.getStudentIds());
        dto.setCatechistIds(classroom.getCatechistIds());
        return dto;
    }

}