package com.programming.management_service.service.student;

import com.programming.management_service.dto.StudentDto;
import com.programming.management_service.dto.UserDto;
import com.programming.management_service.model.Student;

import java.util.List;

public interface StudentService {
    StudentDto createStudent(StudentDto studentDto);

    void addExistingStudentToClassroom(Long classroomId, Long studentId);

    StudentDto createStudentInClassroom(Long classroomId, StudentDto studentDto);

    StudentDto updateStudentInClassroom(Long classroomId, Long studentId, StudentDto updatedStudentDto);

    void removeStudentFromClassroom(Long classroomId, Long studentId);

    List<Student> getStudentsInClassroom(Long classroomId);

    StudentDto convertToDto(Student student);

    void linkUserToStudentByName(String fullName, String saintName, Long userId);

    StudentDto linkStudentWithUser(Long studentId, Long userId);

    StudentDto autoLinkStudentWithUser(UserDto userDto);

}