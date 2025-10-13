package com.programming.user_service.service.student;

import com.programming.common_dto.student.StudentRequestDto;
import com.programming.common_dto.student.StudentResponseDto;
import com.programming.user_service.domain.enums.Role;
import com.programming.user_service.exception.AlreadyExistsException;
import com.programming.user_service.exception.ResourceNotFoundException;
import com.programming.user_service.mapper.StudentMapper;
import com.programming.user_service.domain.model.User;
import com.programming.user_service.domain.model.Student;
import com.programming.user_service.repository.StudentRepository;

import com.programming.user_service.repository.UserRepository;
import com.programming.user_service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public StudentResponseDto createStudent(StudentRequestDto dto) {

        // Check user đã tồn tại chưa (theo fullName + saintName hoặc email)
        if (userRepository.existsByFullNameAndSaintName(dto.getFullName(), dto.getSaintName())) {
            throw new AlreadyExistsException("User already exists");
        }

        // Tạo username tự động
        String generatedUsername = userService.generateUsername(dto.getFullName(), dto.getDateOfBirth());

        if (userRepository.existsByUserName(generatedUsername)) {
            throw new AlreadyExistsException("Username already exists: " + generatedUsername);
        }

        // Tạo User
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setSaintName(dto.getSaintName());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setUserName(generatedUsername);
        user.setPassword(passwordEncoder.encode("defaultPassword")); // hoặc random
        user.setRoles(Set.of(Role.THIEU_NHI));
        userRepository.save(user);

        // Tạo Student và gán user vào
        Student student = studentMapper.toStudentEntity(dto, user);
        studentRepository.save(student);

        return studentMapper.toStudentResponseDto(student);
    }

    @Override
    public StudentResponseDto getStudentById(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for id: " + studentId));

        return studentMapper.toStudentResponseDto(student);
    }


}
