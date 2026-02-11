package com.programming.user_service.service.student;

import com.programming.common.common_dto.student.StudentRequestDto;
import com.programming.common.common_dto.student.StudentResponseDto;

import java.util.List;
import java.util.Set;

public interface StudentService {
    StudentResponseDto createStudent(StudentRequestDto studentDto);

    StudentResponseDto getStudentById(Long studentId);
    
    List<StudentResponseDto> getStudentsByIds(Set<Long> studentIds);

    StudentResponseDto createStudentWithUserId(StudentRequestDto studentDto, Long userId);

    StudentResponseDto updateStudent(Long studentId, StudentRequestDto updatedStudentDto);

    //void updateStudentClassroom(Long studentId, Long classroomId);
}
