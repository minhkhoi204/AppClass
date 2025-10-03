package com.programming.management_service.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.programming.management_service.dto.ClassroomDto;
import com.programming.management_service.dto.StudentDto;
import com.programming.management_service.model.Classroom;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClassroomMapper {

    private final StudentMapper studentMapper;

    public ClassroomDto toClassroomDto(Classroom classroom) {
        if (classroom == null) return null;
        ClassroomDto dto = new ClassroomDto();
        dto.setId(classroom.getId());
        dto.setName(classroom.getName());
        if (classroom.getStudents() != null) {
            List<StudentDto> students = classroom.getStudents()
                    .stream()
                    .map(studentMapper::toStudentDto)
                    .collect(Collectors.toList());
            dto.setStudents(students);
        }
        return dto;
    }

    public Classroom toClassroomEntity(ClassroomDto dto) {
        if (dto == null) return null;
        Classroom c = new Classroom();
        c.setId(dto.getId());
        c.setName(dto.getName());
        // students handling: usually set elsewhere
        return c;
    }
}