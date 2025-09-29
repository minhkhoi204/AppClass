package com.programming.management_service.service.student;

import com.programming.management_service.dto.StudentDto;
import com.programming.management_service.exception.AlreadyExistsException;
import com.programming.management_service.exception.ResourceNotFoundException;
import com.programming.management_service.mapper.StudentMapper;
import com.programming.management_service.model.Classroom;
import com.programming.management_service.model.Student;
import com.programming.management_service.repository.ClassroomRepository;
import com.programming.management_service.repository.StudentRepository;
import com.programming.management_service.management_caller.UserClient;
import com.programming.management_service.dto.UserDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;
    private final ClassroomRepository classroomRepository;
    private final UserClient userClient;

    @Override
    public Student createStudent(StudentDto dto) {
        if (dto.getUserId() != null) {
            Boolean exists = userClient.userExists(dto.getUserId());
            if (!Boolean.TRUE.equals(exists)) {
                throw new ResourceNotFoundException("User not found with id: " + dto.getUserId());
            }

            if (studentRepository.findByUserId(dto.getUserId()).isPresent()) {
                throw new AlreadyExistsException("Student already exists for userId " + dto.getUserId());
            }
        }

        Student student = studentMapper.toStudentEntity(dto);
        student.setUserId(dto.getUserId()); // chỉ set ID, không cần set User entity
        return studentRepository.save(student);
    }


    @Override
    public void addExistingStudentToClassroom(Long classroomId, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + classroomId));

        student.setClassroom(classroom);
        studentRepository.save(student);
    }

    @Override
    public StudentDto createStudentInClassroom(Long classroomId, StudentDto dto) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + classroomId));

        if (dto.getUserId() != null) {
            Boolean exists = userClient.userExists(dto.getUserId());
            if (!Boolean.TRUE.equals(exists)) {
                throw new ResourceNotFoundException("User not found with id: " + dto.getUserId());
            }

            if (studentRepository.findByUserId(dto.getUserId()).isPresent()) {
                throw new AlreadyExistsException("Student already exists for userId " + dto.getUserId());
            }
        }

        Student student = studentMapper.toStudentEntity(dto);
        student.setUserId(dto.getUserId());
        student.setClassroom(classroom);
        Student saved = studentRepository.save(student);
        return studentMapper.toStudentDto(saved);
    }


    @Override
    public StudentDto updateStudentInClassroom(Long classroomId, Long studentId, StudentDto updatedDto) {
        Student existing = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        if (existing.getClassroom() == null || !existing.getClassroom().getId().equals(classroomId)) {
            throw new IllegalStateException("Student does not belong to this classroom");
        }

        // update fields from DTO
        existing.setSaintName(updatedDto.getSaintName());
        existing.setFullName(updatedDto.getFullName());
        existing.setFatherName(updatedDto.getFatherName());
        existing.setFatherPhoneNum(updatedDto.getFatherPhoneNum());
        existing.setMotherName(updatedDto.getMotherName());
        existing.setMotherPhoneNum(updatedDto.getMotherPhoneNum());
        existing.setAddress(updatedDto.getAddress());
        existing.setDateOfBirth(updatedDto.getDateOfBirth());

        Student saved = studentRepository.save(existing);

        return studentMapper.toStudentDto(saved);
    }

    @Override
    public StudentDto convertToDto(Student student) {
        return studentMapper.toStudentDto(student);
    }

    @Override
    public void removeStudentFromClassroom(Long classroomId, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        if (student.getClassroom() == null || !student.getClassroom().getId().equals(classroomId)) {
            throw new IllegalStateException("Student does not belong to this classroom");
        }

        student.setClassroom(null);
        studentRepository.save(student); // just remove student from class
    }

    @Override
    public List<Student> getStudentsInClassroom(Long classroomId) {
        // Check if there isn't any classroom
        if (!classroomRepository.existsById(classroomId)) {
            throw new ResourceNotFoundException("Classroom not found with id: " + classroomId);
        }

        return studentRepository.findAllByClassroomId(classroomId);
    }
}
