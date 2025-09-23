package com.programming.user_service.service.student;

import com.programming.user_service.dto.StudentDto;
import com.programming.user_service.exceptions.AlreadyExistsException;
import com.programming.user_service.exceptions.ResourceNotFoundException;
import com.programming.user_service.model.Classroom;
import com.programming.user_service.model.Student;
import com.programming.user_service.model.User;
import com.programming.user_service.repository.ClassroomRepository;
import com.programming.user_service.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;
    private final ClassroomRepository classroomRepository; // 👈 inject vào đây


    @Override
    public Student createStudent(Student student) {
        if (studentRepository.existsById(student.getUserId())) {
            throw new AlreadyExistsException("Student already exists for userId " + student.getUserId());
        }
        return studentRepository.save(student);
    }


    @Override
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student updateStudent(Long id, Student studentDetails) {
        return studentRepository.findById(id).map(student -> {
            student.setFatherName(studentDetails.getFatherName());
            student.setFatherPhoneNum(studentDetails.getFatherPhoneNum());
            student.setMotherName(studentDetails.getMotherName());
            student.setMotherPhoneNum(studentDetails.getMotherPhoneNum());
            student.setAddress(studentDetails.getAddress());
            student.setClassroom(studentDetails.getClassroom());
            return studentRepository.save(student);
        }).orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }


    /*public StudentDto convertToDto(Student student) {
        User user = student.getUser();

        StudentDto dto = new StudentDto();
        dto.setUserId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setSaintName(user.getSaintName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setFatherName(student.getFatherName());
        dto.setFatherPhoneNum(student.getFatherPhoneNum());
        dto.setMotherName(student.getMotherName());
        dto.setMotherPhoneNum(student.getMotherPhoneNum());
        dto.setAddress(student.getAddress());
        dto.setClassJoined(student.getClassJoined());

        return dto;
    }*/
    @Override
    public StudentDto convertToDto(Student student) {
        StudentDto studentDto = modelMapper.map(student, StudentDto.class);

        User user = student.getUser();
        if (user != null) {
            studentDto.setUserId(user.getId());
            studentDto.setSaintName(user.getSaintName());
            studentDto.setFullName(user.getFullName());
            studentDto.setEmail(user.getEmail());
            studentDto.setPhone(user.getPhone());
        }

        if (student.getClassroom() != null) {
            studentDto.setClassroomName(student.getClassroom().getName());
        }

        return studentDto;
    }

    @Override
    public void addStudentToClassroom(Long classroomId, Student student) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + classroomId));

        student.setClassroom(classroom);
        studentRepository.save(student);
    }

    @Override
    public void removeStudentFromClassroom(Long classroomId, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        if (student.getClassroom() == null || !student.getClassroom().getId().equals(classroomId)) {
            throw new IllegalStateException("Student does not belong to this classroom");
        }

        student.setClassroom(null);
        studentRepository.save(student); // không xóa luôn student khỏi DB, chỉ xóa khỏi lớp
    }

    @Override
    public void updateStudentInClassroom(Long classroomId, Long studentId, Student updatedStudent) {
        Student existing = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        if (!existing.getClassroom().getId().equals(classroomId)) {
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
