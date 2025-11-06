package com.programming.management_service.service.classroom;

import com.programming.management_service.domain.dto.response.ClassroomResponseDto;
import com.programming.management_service.domain.dto.request.ClassroomRequestDto;
import com.programming.management_service.domain.model.Classroom;

import java.util.List;

public interface ClassroomService {
    ClassroomResponseDto createClassroom(ClassroomRequestDto dto);

    ClassroomResponseDto getClassroomById(Long id);

    ClassroomResponseDto updateClassroom(Long id, ClassroomRequestDto requestDto);

    void addStudentToClassroom(Long classroomId, Long studentId);
    
    void removeStudentFromClassroom(Long classroomId, Long studentId);
    
    void addCatechistToClassroom(Long classroomId, Long catechistId);
    
    void removeCatechistFromClassroom(Long classroomId, Long catechistId);

}