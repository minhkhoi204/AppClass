package com.programming.user_service.service.student;

import com.programming.common_dto.student.StudentRequestDto;
import com.programming.common_dto.student.StudentResponseDto;

public interface StudentService {
    StudentResponseDto createStudent(StudentRequestDto studentDto);

    StudentResponseDto getStudentById(Long studentId);

    StudentResponseDto createStudentWithUserId(StudentRequestDto studentDto, Long userId);
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
