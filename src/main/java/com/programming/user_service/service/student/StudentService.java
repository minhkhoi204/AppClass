package com.programming.user_service.service.student;

import com.programming.user_service.dto.StudentDto;
import com.programming.user_service.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student createStudent(StudentDto studentDto);

    void addExistingStudentToClassroom(Long classroomId, Long studentId);

    StudentDto createStudentInClassroom(Long classroomId, StudentDto studentDto);

    StudentDto updateStudentInClassroom(Long classroomId, Long studentId, StudentDto updatedStudentDto);

    void removeStudentFromClassroom(Long classroomId, Long studentId);

    void updateStudentInClassroom(Long classroomId, Long studentId, Student updatedStudent);

    List<Student> getStudentsInClassroom(Long classroomId);

    StudentDto convertToDto(Student student);

}