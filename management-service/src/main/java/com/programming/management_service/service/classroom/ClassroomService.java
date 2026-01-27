package com.programming.management_service.service.classroom;

import com.programming.common.common_dto.catechist.CatechistResponseDto;
import com.programming.common.common_dto.student.StudentResponseDto;
import com.programming.management_service.domain.dto.response.ClassroomResponseDto;
import com.programming.management_service.domain.dto.request.ClassroomRequestDto;
import com.programming.management_service.domain.model.Classroom;

import java.util.List;

public interface ClassroomService {
    ClassroomResponseDto createClassroom(ClassroomRequestDto dto);

    ClassroomResponseDto getClassroomById(Long id);

    ClassroomResponseDto updateClassroom(Long id, ClassroomRequestDto requestDto);

    // void addStudentToClassroom(Long classroomId, Long studentId);
    
    // void removeStudentFromClassroom(Long classroomId, Long studentId);
    
    // void addCatechistToClassroom(Long classroomId, Long catechistId);
    
    // void removeCatechistFromClassroom(Long classroomId, Long catechistId);

    List<Long> getStudentIds(Long classroomId, String academicYear);

    List<Long> getCatechistIds(Long classroomId, String academicYear);

    int getStudentCount(Long classroomId, String academicYear);
    
    int getCatechistCount(Long classroomId, String academicYear);

    List<StudentResponseDto> getStudentsInClassroom(Long classroomId, String academicYear);
    
    List<CatechistResponseDto> getCatechistsInClassroom(Long classroomId, String academicYear);

}