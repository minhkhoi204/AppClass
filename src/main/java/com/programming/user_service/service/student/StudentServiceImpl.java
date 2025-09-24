package com.programming.user_service.service.student;

import com.programming.user_service.dto.StudentDto;
import com.programming.user_service.exceptions.AlreadyExistsException;
import com.programming.user_service.exceptions.ResourceNotFoundException;
import com.programming.user_service.mapper.StudentMapper;
import com.programming.user_service.model.Classroom;
import com.programming.user_service.model.Student;
import com.programming.user_service.model.User;
import com.programming.user_service.repository.ClassroomRepository;
import com.programming.user_service.repository.StudentRepository;
import com.programming.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;
    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;

    @Override
    public Student createStudent(StudentDto dto) {
        // Nếu có User thì lấy ra từ DB, còn không thì student sẽ chưa có user
        User user = null;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

            // Check nếu user này đã gắn với 1 student rồi
            if (studentRepository.findByUserId(dto.getUserId()).isPresent()) {
                throw new AlreadyExistsException("Student already exists for userId " + dto.getUserId());
            }
        }

        Student student = studentMapper.toEntity(dto);
        student.setUser(user);

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

        // Tạo student mới từ DTO
        Student student = studentMapper.toEntity(dto);

        // Gán classroom
        student.setClassroom(classroom);

        // Không cần xử lý user
        Student saved = studentRepository.save(student);

        // Trả về DTO để client có thông tin (id, classroomName...)
        return studentMapper.toStudentDto(saved);
    }

    @Override
    public StudentDto updateStudentInClassroom(Long classroomId, Long studentId, StudentDto updatedDto) {
        Student existing = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        if (existing.getClassroom() == null || !existing.getClassroom().getId().equals(classroomId)) {
            throw new IllegalStateException("Student does not belong to this classroom");
        }

        // Cập nhật các trường cần thiết từ DTO\
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
        studentRepository.save(student); // không xóa student khỏi DB, chỉ bỏ khỏi lớp
    }

    @Override
    public void updateStudentInClassroom(Long classroomId, Long studentId, Student updatedStudent) {
        Student existing = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        if (existing.getClassroom() == null || !existing.getClassroom().getId().equals(classroomId)) {
            throw new IllegalStateException("Student does not belong to this classroom");
        }

        existing.setFatherName(updatedStudent.getFatherName());
        existing.setFatherPhoneNum(updatedStudent.getFatherPhoneNum());
        existing.setMotherName(updatedStudent.getMotherName());
        existing.setMotherPhoneNum(updatedStudent.getMotherPhoneNum());
        existing.setAddress(updatedStudent.getAddress());

        studentRepository.save(existing);
    }

    @Override
    public List<Student> getStudentsInClassroom(Long classroomId) {
        return studentRepository.findAllByClassroomId(classroomId);
    }
}
