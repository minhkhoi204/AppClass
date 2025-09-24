package com.programming.user_service.mapper;

import org.springframework.stereotype.Component;
import com.programming.user_service.dto.StudentDto;
import com.programming.user_service.model.Student;
import com.programming.user_service.model.User;

@Component
public class StudentMapper {

    public StudentDto toStudentDto(Student student) {
        if (student == null) return null;
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

        if (student.getClassroom() != null) {
            dto.setClassroomName(student.getClassroom().getName());
        }
        if (student.getUser() != null) {
            dto.setUserId(student.getUser().getId());
        }

        return dto;
    }

    /**
     * Chỉ map các field của Student (không set User, không set id).
     * Caller (service) phải set user: student.setUser(foundUser)
     * vì Student dùng @MapsId.
     */
    public Student toEntity(StudentDto dto) {
        if (dto == null) return null;
        Student student = new Student();
        student.setFullName(dto.getFullName());
        student.setSaintName(dto.getSaintName());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setFatherName(dto.getFatherName());
        student.setFatherPhoneNum(dto.getFatherPhoneNum());
        student.setMotherName(dto.getMotherName());
        student.setMotherPhoneNum(dto.getMotherPhoneNum());
        student.setAddress(dto.getAddress());
        return student;
    }
}
