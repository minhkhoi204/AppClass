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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;
    private final ClassroomRepository classroomRepository;
    private final UserClient userClient;

    @Override
    public StudentDto createStudent(StudentDto dto) {
        if (dto.getUserId() != null) {
            Boolean exists = userClient.userExists(dto.getUserId()); // gọi sang user-service
            if (!Boolean.TRUE.equals(exists)) {
                throw new ResourceNotFoundException("User not found with id: " + dto.getUserId());
            }

            if (studentRepository.findByUserId(dto.getUserId()).isPresent()) {
                throw new AlreadyExistsException("Student already exists for userId " + dto.getUserId());
            }
        }

        Student student = studentMapper.toStudentEntity(dto); // convert DTO → Entity
        student.setUserId(dto.getUserId()); // không set User entity, chỉ set userId (OK)

        Student saved = studentRepository.save(student); // lưu DB

        return studentMapper.toStudentDto(saved); // convert lại → DTO để trả ra controller
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

    @Override
    @Transactional
    public void linkUserToStudentByName(String fullName, String saintName, Long userId) {
        List<Student> candidates = studentRepository.findByFullNameIgnoreCase(fullName.trim());

        if (candidates.isEmpty()) {
            throw new ResourceNotFoundException("No student found with name: " + fullName);
        }

        Student student = null;

        if (saintName != null) {
            student = candidates.stream()
                    .filter(s -> saintName.equalsIgnoreCase(s.getSaintName()))
                    .findFirst()
                    .orElse(null);
        } else if (candidates.size() == 1) {
            student = candidates.get(0);
        }

        if (student == null) {
            throw new IllegalStateException("Multiple students found or saintName mismatch");
        }

        if (student.getUserId() != null) {
            throw new AlreadyExistsException("Student already linked to a user");
        }

        if (!Boolean.TRUE.equals(userClient.userExists(userId))) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        if (studentRepository.existsByUserId(userId)) {
            throw new AlreadyExistsException("User already linked to another student");
        }

        student.setUserId(userId);
        studentRepository.save(student);
    }


    @Override
    public StudentDto linkStudentWithUser(Long studentId, Long userId) {
        // Check user tồn tại thông qua Feign client
        if (!Boolean.TRUE.equals(userClient.userExists(userId))) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        if (student.getUserId() != null) {
            throw new AlreadyExistsException("Student is already linked to a user");
        }

        student.setUserId(userId);
        Student saved = studentRepository.save(student);
        return studentMapper.toStudentDto(saved);
    }

    @Override
    public StudentDto autoLinkStudentWithUser(UserDto userDto) {
        List<Student> candidates = studentRepository.findAll().stream()
                .filter(s -> s.getUserId() == null &&
                        s.getFullName().equalsIgnoreCase(userDto.getFullName()))
                .collect(Collectors.toList());

        if (candidates.isEmpty()) {
            throw new ResourceNotFoundException("No matching student found to link");
        }

        if (candidates.size() > 1) {
            throw new IllegalStateException("Multiple matching students found. Please link manually.");
        }

        Student student = candidates.get(0);

        if (!Boolean.TRUE.equals(userClient.userExists(userDto.getId()))) {
            throw new ResourceNotFoundException("User not found with id: " + userDto.getId());
        }

        student.setUserId(userDto.getId());
        Student saved = studentRepository.save(student);
        return studentMapper.toStudentDto(saved);
    }

}
