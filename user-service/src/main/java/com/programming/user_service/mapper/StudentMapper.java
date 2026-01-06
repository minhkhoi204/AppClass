package com.programming.user_service.mapper;

import com.programming.common.common_dto.student.StudentRequestDto;
import com.programming.common.common_dto.student.StudentResponseDto;
import com.programming.user_service.domain.model.Student;
import com.programming.user_service.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor

public class StudentMapper {

    public StudentResponseDto toStudentResponseDto(Student student) {
        if (student == null) return null;

        return StudentResponseDto.builder()
                .userId(student.getUser().getId())
                .id(student.getId())
                .fullName(student.getUser().getFullName())
                .christianName(student.getUser().getChristianName())
                .dateOfBirth(student.getUser().getDateOfBirth())
                .fatherName(student.getFatherName())
                .fatherPhoneNum(student.getFatherPhoneNum())
                .motherName(student.getMotherName())
                .motherPhoneNum(student.getMotherPhoneNum())
                .address(student.getAddress())
                //.classroomName(student.getClassroom() != null ? student.getClassroom().getName() : null)
                .classroomId(student.getClassroomId())
                .studentCode(student.getStudentCode())
                .build();
    }

    public Student toStudentEntity(StudentRequestDto dto, User user) { //input dto request to entity to save in db
        Student student = new Student();
        student.setUser(user);
        student.setFatherName(dto.getFatherName());
        student.setFatherPhoneNum(dto.getFatherPhoneNum());
        student.setMotherName(dto.getMotherName());
        student.setMotherPhoneNum(dto.getMotherPhoneNum());
        student.setAddress(dto.getAddress());
        student.setStudentCode(dto.getStudentCode());
        ///student.setClassroomId(dto.getClassroomId());
        return student;
    }

}