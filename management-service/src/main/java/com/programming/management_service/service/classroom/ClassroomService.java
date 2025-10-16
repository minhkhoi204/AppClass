package com.programming.management_service.service.classroom;

import com.programming.management_service.domain.dto.ClassroomDto;
import com.programming.management_service.domain.dto.request.ClassroomRequestDto;
import com.programming.management_service.domain.dto.response.ClassroomResponseDto;
import com.programming.management_service.domain.model.Classroom;

import java.util.List;

public interface ClassroomService {
    Classroom createClassroom(Classroom classroom);
    ClassroomResponseDto getClassroomById(Long id);
//    List<Classroom> getAllClassrooms();
    ClassroomResponseDto updateClassroom(Long id, ClassroomRequestDto requestDto);
//    void deleteClassroom(Long id);
//
//    ClassroomDto getClassroomWithDetails(Long id);


    ClassroomResponseDto addStudentToClassroom(Long classroomId, Long studentId);
}