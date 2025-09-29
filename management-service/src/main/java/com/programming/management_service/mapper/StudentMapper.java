package com.programming.management_service.mapper;

import org.springframework.stereotype.Component;
import com.programming.management_service.dto.StudentDto;
import com.programming.management_service.model.Student;

@Component
public class StudentMapper {
    public Student toStudentEntity(StudentDto dto) {
        Student student = new Student();
        student.setFullName(dto.getFullName());
        student.setSaintName(dto.getSaintName());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setFatherName(dto.getFatherName());
        student.setFatherPhoneNum(dto.getFatherPhoneNum());
        student.setMotherName(dto.getMotherName());
        student.setMotherPhoneNum(dto.getMotherPhoneNum());
        student.setAddress(dto.getAddress());
        student.setUserId(dto.getUserId());
        return student;
    }

    public StudentDto toStudentDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setFullName(student.getFullName());
        dto.setSaintName(student.getSaintName());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setFatherName(student.getFatherName());
        dto.setFatherPhoneNum(student.getFatherPhoneNum());
        dto.setMotherName(student.getMotherName());
        dto.setMotherPhoneNum(student.getMotherPhoneNum());
        dto.setAddress(student.getAddress());
        dto.setUserId(student.getUserId());
        dto.setClassroomName(student.getClassroom() != null ? student.getClassroom().getName() : null);
        return dto;
    }
}
