package com.programming.management_service.service.classroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programming.common_dto.student.StudentRequestDto;
import com.programming.common_dto.student.StudentResponseDto;
import com.programming.management_service.domain.dto.ClassroomDto;
import com.programming.management_service.domain.dto.request.ClassroomRequestDto;
import com.programming.management_service.domain.dto.response.ClassroomResponseDto;
import com.programming.management_service.exception.AlreadyExistsException;
import com.programming.management_service.exception.ResourceNotFoundException;
import com.programming.management_service.domain.model.Classroom;
import com.programming.management_service.management_caller.StudentClient;
import com.programming.management_service.mapper.ClassroomMapper;
import com.programming.management_service.repository.ClassroomRepository;
import com.programming.management_service.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final StudentClient studentClient;
    private final ClassroomMapper classroomMapper;
    private final ObjectMapper objectMapper;


    @Override
    public ClassroomResponseDto createClassroom(ClassroomRequestDto dto) {
        if (classroomRepository.existsByName(dto.getName())) {
            throw new AlreadyExistsException("Classroom: '" + dto.getName() + "' already exists");
        }

        Classroom classroom = classroomMapper.toClassroomEntity(dto);
        Classroom saved = classroomRepository.save(classroom);
        return classroomMapper.toClassroomResponseDto(saved);
    }

    private List<StudentResponseDto> getStudentsInClassroom(Classroom classroom) {
        return classroom.getStudentIds().stream()
                .map(studentId -> {
                    ApiResponse response = studentClient.getStudentById(studentId);
                    return objectMapper.convertValue(response.getData(), StudentResponseDto.class);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ClassroomResponseDto getClassroomById(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + id));

        ClassroomResponseDto dto = classroomMapper.toClassroomResponseDto(classroom);
        dto.setStudents(getStudentsInClassroom(classroom));
        return dto;
    }

    @Override
    public void addStudentToClassroom(Long classroomId, Long studentId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

        if (classroom.getStudentIds().contains(studentId)) {
            throw new AlreadyExistsException("Student with ID " + studentId + " is already in the classroom");
        }

        // Validate student exists
        ApiResponse response = studentClient.getStudentById(studentId);
        StudentResponseDto student = objectMapper.convertValue(response.getData(), StudentResponseDto.class);

        classroom.getStudentIds().add(student.getId());
        classroomRepository.save(classroom);

        StudentRequestDto updatedStudentDto = new StudentRequestDto();
        updatedStudentDto.setClassroomId(classroomId);

        studentClient.updateStudent(studentId, updatedStudentDto);
    }


//    @Override
//    public ClassroomResponseDto addStudentToClassroom(Long classroomId, Long studentId) {
//        Classroom classroom = classroomRepository.findById(classroomId)
//                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));
//
//        if (classroom.getStudentIds().contains(studentId)) {
//            throw new AlreadyExistsException("Student with ID " + studentId + " is already in the classroom");
//        }
//
//        // call user-service
//        ApiResponse response = studentClient.getStudentById(studentId);
//
//        // Convert object -> StudentResponseDto
//        StudentResponseDto student = objectMapper.convertValue(response.getData(), StudentResponseDto.class);
//
//        classroom.getStudentIds().add(student.getId());
//        classroomRepository.save(classroom);
//
//        return getClassroomById(classroomId);
//    }

    @Override
    public ClassroomResponseDto updateClassroom(Long id, ClassroomRequestDto requestDto) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + id));

        // cập nhật thông tin từ request
        classroom.setName(requestDto.getName());
        //classroom.setStudentIds(requestDto.getStudentIds() != null ? new HashSet<>(requestDto.getStudentIds()) : new HashSet<>());
        if (requestDto.getStudentIds() != null) {
            classroom.setStudentIds(new HashSet<>(requestDto.getStudentIds()));
        }

        Classroom updated = classroomRepository.save(classroom);
        return classroomMapper.toClassroomResponseDto(updated);
    }

}
