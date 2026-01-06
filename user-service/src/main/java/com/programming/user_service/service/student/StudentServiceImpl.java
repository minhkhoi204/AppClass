package com.programming.user_service.service.student;

import com.programming.common.common_dto.student.StudentRequestDto;
import com.programming.common.common_dto.student.StudentResponseDto;
import com.programming.common.common_auth.Role;
import com.programming.common.common_auth.RoleUtils;
import com.programming.common.exception.AlreadyExistsException;
import com.programming.common.exception.IllegalStateException;
import com.programming.common.exception.ResourceNotFoundException;
import com.programming.user_service.mapper.StudentMapper;
import com.programming.user_service.domain.model.User;
import com.programming.user_service.domain.model.Student;
import com.programming.user_service.repository.StudentRepository;

import com.programming.user_service.repository.UserRepository;
import com.programming.user_service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        // check if student exists (by fullName + christianName)
        if (userRepository.existsByFullNameAndChristianName(dto.getFullName(), dto.getChristianName())) {
            throw new AlreadyExistsException("User already exists");
        }

        // create username
        String generatedUsername = userService.generateUsername(dto.getFullName(), dto.getDateOfBirth());

        if (userRepository.existsByUserName(generatedUsername)) {
            throw new AlreadyExistsException("Username already exists: " + generatedUsername);
        }

        // create User
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setChristianName(dto.getChristianName());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setUserName(generatedUsername);
        user.setPassword(passwordEncoder.encode("defaultPassword")); // or random

        // set role of user
        user.setRole(Role.THIEU_NHI);
        
        userRepository.save(user);

        // create Student and assign user
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

    @Override //check again about checking userid instead
    public StudentResponseDto createStudentWithUserId(StudentRequestDto studentDto, Long userId) {
        // check user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if(studentRepository.existsByUserId(userId)){
            throw new AlreadyExistsException("Student already exists for this user.");
        }

        // check if user is already catechist
        //if (user.getRole() != null && RoleUtils.isCatechist(user.getRole())) {
        if (user.getRole() != null) {
            throw new IllegalStateException("User is already has role.");
        }

        if (!user.getFullName().equals(studentDto.getFullName()) ||
                !user.getChristianName().equals(studentDto.getChristianName())) {
            throw new IllegalStateException("Provided fullName or christianName does not match with the user.");
        }

        // set role of user to THIEU_NHI
        user.setRole(Role.THIEU_NHI);
        userRepository.save(user);

        Student student = studentMapper.toStudentEntity(studentDto, user);
        studentRepository.save(student);
        return studentMapper.toStudentResponseDto(student);
    }

    @Override
    public StudentResponseDto updateStudent(Long studentId, StudentRequestDto updatedStudentDto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        if (updatedStudentDto.getClassroomId() != null) {
            student.setClassroomId(updatedStudentDto.getClassroomId());
        }
        
        if (updatedStudentDto.getStudentCode() != null) {
            student.setStudentCode(updatedStudentDto.getStudentCode());
        }

        // prevent overwrite null
        if (updatedStudentDto.getFatherName() != null) {
            student.setFatherName(updatedStudentDto.getFatherName());
        }
        if (updatedStudentDto.getMotherName() != null) {
            student.setMotherName(updatedStudentDto.getMotherName());
        }
        if (updatedStudentDto.getFatherPhoneNum() != null) {
            student.setFatherPhoneNum(updatedStudentDto.getFatherPhoneNum());
        }
        if (updatedStudentDto.getMotherPhoneNum() != null) {
            student.setMotherPhoneNum(updatedStudentDto.getMotherPhoneNum());
        }
        if (updatedStudentDto.getAddress() != null) {
            student.setAddress(updatedStudentDto.getAddress());
        }

        User user = student.getUser();
        if (updatedStudentDto.getFullName() != null) {
            user.setFullName(updatedStudentDto.getFullName());
        }
        if (updatedStudentDto.getChristianName() != null) {
            user.setChristianName(updatedStudentDto.getChristianName());
        }
        if (updatedStudentDto.getDateOfBirth() != null) {
            user.setDateOfBirth(updatedStudentDto.getDateOfBirth());
        }

        userRepository.save(user);
        student = studentRepository.save(student);

        return studentMapper.toStudentResponseDto(student);
    }


//    @Override
//    public void updateStudentClassroom(Long studentId, Long classroomId) {
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
//
//        student.setClassroomId(classroomId);
//        studentRepository.save(student);
//    }
}
