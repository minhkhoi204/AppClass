package com.programming.user_service.service.student;

import com.programming.user_service.dto.StudentDto;
import com.programming.user_service.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student createStudent(Student student);
    Optional<Student> getStudentById(Long id);
    List<Student> getAllStudents();
    Student updateStudent(Long id, Student studentDetails);
    void deleteStudent(Long id);

    void addStudentToClassroom(Long classroomId, Student student);
    void removeStudentFromClassroom(Long classroomId, Long studentId);
    void updateStudentInClassroom(Long classroomId, Long studentId, Student updatedStudent);
    List<Student> getStudentsInClassroom(Long classroomId);

    StudentDto convertToDto(Student student);

}