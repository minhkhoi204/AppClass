package com.programming.user_service.service.student;

import com.programming.common.common_dto.student.StudentRequestDto;
import com.programming.common.common_dto.student.StudentResponseDto;

public interface StudentService {
    StudentResponseDto createStudent(StudentRequestDto studentDto);

    StudentResponseDto getStudentById(Long studentId);

    StudentResponseDto createStudentWithUserId(StudentRequestDto studentDto, Long userId);

    StudentResponseDto updateStudent(Long studentId, StudentRequestDto updatedStudentDto);

    //void updateStudentClassroom(Long studentId, Long classroomId);
}
