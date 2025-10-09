package com.programming.management_service.service.classroom;

import com.programming.management_service.dto.ClassroomDto;
import com.programming.management_service.dto.StudentDto;
import com.programming.management_service.exception.AlreadyExistsException;
import com.programming.management_service.exception.ResourceNotFoundException;
import com.programming.management_service.model.Classroom;
import com.programming.management_service.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;


    @Override
    public Classroom createClassroom(Classroom classroom) {
        return Optional.of(classroom)
                .filter(c -> !classroomRepository.existsByName(c.getName())) // hoặc byCode, byId...
                .map(classroomRepository::save)
                .orElseThrow(() -> new AlreadyExistsException("Classroom: '" + classroom.getName() + "' already exists"));
    }

    @Override
    public Classroom getClassroomById(Long id) {
        //check
        return classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + id));
    }

    @Override
    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    @Override
    public Classroom updateClassroom(Long id, Classroom classroomDetails) {
        return classroomRepository.findById(id).map(classroom -> {
            classroom.setName(classroomDetails.getName());
            return classroomRepository.save(classroom);
        }).orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + id));
    }

    @Override
    public void deleteClassroom(Long id) {
        if (!classroomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Classroom not found with id: " + id);
        }
        classroomRepository.deleteById(id);
    }

    @Override
    public ClassroomDto getClassroomWithDetails(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + id));

        ClassroomDto dto = new ClassroomDto();
        dto.setId(classroom.getId());
        dto.setName(classroom.getName());

        // Lấy danh sách student từ repository
//        List<Student> students = studentRepository.findAllByClassroomId(id);
//        List<StudentDto> studentDtos = students.stream()
//                .map(studentMapper::toStudentDto)
//                .toList(); // Java 16+ hoặc dùng collect(Collectors.toList()) nếu Java < 16

//        dto.setStudents(studentDtos);


        return dto;
    }
}
