package com.programming.user_service.service.student;

import com.programming.user_service.domain.dto.request.UserRequestDto;
import com.programming.user_service.domain.model.Student;
import com.programming.user_service.domain.dto.request.StudentRequestDto;
import com.programming.user_service.domain.dto.response.StudentResponseDto;

import java.util.List;

public interface StudentService {
    StudentResponseDto createStudent(StudentRequestDto studentDto);

    //void addExistingStudentToClassroom(Long classroomId, Long studentId);

//    StudentResponseDto createStudentInClassroom(Long classroomId, StudentRequestDto studentDto);
//
//    StudentResponseDto updateStudentInClassroom(Long classroomId, Long studentId, StudentRequestDto updatedStudentDto);
//
//    void removeStudentFromClassroom(Long classroomId, Long studentId);
//
//    List<Student> getStudentsInClassroom(Long classroomId);
//
//    StudentResponseDto convertToDto(Student student);
//
//    void linkUserToStudentByName(String fullName, String saintName, Long userId);
//
//    StudentResponseDto linkStudentWithUser(Long studentId, Long userId);
//
//    StudentResponseDto autoLinkStudentWithUser(UserRequestDto userDto);
//
//    StudentResponseDto getStudentWithClassroom(Long studentId);
}
