package com.programming.management_service.service.classroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programming.common.common_dto.catechist.CatechistRequestDto;
import com.programming.common.common_dto.catechist.CatechistResponseDto;
import com.programming.common.common_dto.student.StudentRequestDto;
import com.programming.common.common_dto.student.StudentResponseDto;

import com.programming.common.exception.AlreadyExistsException;
import com.programming.common.exception.ResourceNotFoundException;
import com.programming.common.response.ApiResponse;

import com.programming.management_service.domain.dto.request.ClassroomRequestDto;
import com.programming.management_service.domain.dto.response.ClassroomResponseDto;
import com.programming.management_service.domain.model.AssignmentStatus;
import com.programming.management_service.domain.model.Classroom;
import com.programming.management_service.management_caller.CatechistClient;
import com.programming.management_service.management_caller.StudentClient;
import com.programming.management_service.mapper.ClassroomMapper;
import com.programming.management_service.repository.ClassroomAssignmentRepository;
import com.programming.management_service.repository.ClassroomRepository;
import com.programming.management_service.repository.EnrollmentRepository;

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
    private final EnrollmentRepository enrollmentRepository;
    private final ClassroomAssignmentRepository assignmentRepository;
    private final StudentClient studentClient;
    private final CatechistClient catechistClient;
    private final ClassroomMapper classroomMapper;
    private final ObjectMapper objectMapper;

    @Override
    public ClassroomResponseDto createClassroom(ClassroomRequestDto dto) {
        if (!classroomRepository.findByNameAndAcademicYear(dto.getName(), dto.getAcademicYear()).isEmpty()) {
            throw new AlreadyExistsException("Classroom: '" + dto.getName() + "' already exists for academic year: " + dto.getAcademicYear());
        }

        Classroom classroom = classroomMapper.toClassroomEntity(dto);
        Classroom saved = classroomRepository.save(classroom);
        return classroomMapper.toClassroomResponseDto(saved);
    }
    /* 
    private List<StudentResponseDto> getStudentsInClassroom(Classroom classroom) {
        return classroom.getStudentIds().stream()
                .map(studentId -> {
                    ApiResponse response = studentClient.getStudentById(studentId);
                    return objectMapper.convertValue(response.getData(), StudentResponseDto.class);
                })
                .collect(Collectors.toList());
    }
    */

    @Override
    public ClassroomResponseDto getClassroomById(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + id));

        ClassroomResponseDto dto = classroomMapper.toClassroomResponseDto(classroom);
        //dto.setStudents(getStudentsInClassroom(classroom));
        return dto;
    }

    /* 
    @Override
    public void addStudentToClassroom(Long classroomId, Long studentId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

        if (classroom.getStudentIds().contains(studentId)) {
            throw new AlreadyExistsException("Student with ID " + studentId + " is already in the classroom");
        }

        // Check max students limit
        //if (classroom.getMaxStudents() != null && 
        //    classroom.getStudentIds().size() >= classroom.getMaxStudents()) {
        //    throw new IllegalStateException("Classroom has reached maximum capacity: " + classroom.getMaxStudents());
        //}

        // Validate student exists
        ApiResponse response = studentClient.getStudentById(studentId);
        StudentResponseDto student = objectMapper.convertValue(response.getData(), StudentResponseDto.class);

        classroom.getStudentIds().add(student.getId());
        classroomRepository.save(classroom);

        StudentRequestDto updatedStudentDto = new StudentRequestDto();
        updatedStudentDto.setClassroomId(classroomId);

        studentClient.updateStudent(studentId, updatedStudentDto);
    }
    */


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

        // update info from request
        if (requestDto.getName() != null) {
            classroom.setName(requestDto.getName());
        }
        if (requestDto.getLevel() != null) {
            classroom.setLevel(requestDto.getLevel());
        }
        if (requestDto.getAcademicYear() != null) {
            classroom.setAcademicYear(requestDto.getAcademicYear());
        }
        if (requestDto.getRoom() != null) {
            classroom.setRoom(requestDto.getRoom());
        }
        //if (requestDto.getMaxStudents() != null) {
        //    classroom.setMaxStudents(requestDto.getMaxStudents());
        //}
        if (requestDto.getSchedule() != null) {
            classroom.setSchedule(requestDto.getSchedule());
        }
        if (requestDto.getNote() != null) {
            classroom.setNote(requestDto.getNote());
        }
        // if (requestDto.getStudentIds() != null) {
        //     classroom.setStudentIds(new HashSet<>(requestDto.getStudentIds()));
        // }
        // if (requestDto.getCatechistIds() != null) {
        //     classroom.setCatechistIds(new HashSet<>(requestDto.getCatechistIds()));
        // }

        Classroom updated = classroomRepository.save(classroom);
        return classroomMapper.toClassroomResponseDto(updated);
    }

    /*
    @Override
    public void removeStudentFromClassroom(Long classroomId, Long studentId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

        if (!classroom.getStudentIds().contains(studentId)) {
            throw new ResourceNotFoundException("Student with ID " + studentId + " is not in this classroom");
        }

        classroom.getStudentIds().remove(studentId);
        classroomRepository.save(classroom);

        // Update student's classroomId to null
        StudentRequestDto updatedStudentDto = new StudentRequestDto();
        updatedStudentDto.setClassroomId(null);
        studentClient.updateStudent(studentId, updatedStudentDto);
    }
    */

    /*
    @Override
    public void addCatechistToClassroom(Long classroomId, Long catechistId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

        if (classroom.getCatechistIds().contains(catechistId)) {
            throw new AlreadyExistsException("Catechist with ID " + catechistId + " is already in this classroom");
        }

        // Validate catechist exists
        //catechistClient.getCatechistById(catechistId);
        ApiResponse response = catechistClient.getCatechistById(catechistId);
        CatechistResponseDto catechist = objectMapper.convertValue(response.getData(), CatechistResponseDto.class);

        //classroom.getCatechistIds().add(catechistId);
        classroom.getCatechistIds().add(catechist.getId());
        classroomRepository.save(classroom);

        CatechistRequestDto updatedCatechistDto = new CatechistRequestDto();
        updatedCatechistDto.setClassroomId(classroomId);

        catechistClient.updateCatechist(catechistId, updatedCatechistDto);
    }
    */

    /*
    @Override
    public void removeCatechistFromClassroom(Long classroomId, Long catechistId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

        if (!classroom.getCatechistIds().contains(catechistId)) {
            throw new ResourceNotFoundException("Catechist with ID " + catechistId + " is not in this classroom");
        }

        classroom.getCatechistIds().remove(catechistId);
        classroomRepository.save(classroom);
    }
    */

    @Override
    public List<Long> getStudentIds(Long classroomId, String academicYear) {
        return enrollmentRepository.findByClassroomIdAndAcademicYear(classroomId, academicYear)
                .stream()
                .map(enrollment -> enrollment.getStudentId())
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getCatechistIds(Long classroomId, String academicYear) {
        return assignmentRepository.findByClassroomIdAndAcademicYearAndStatus(
                        classroomId, academicYear, AssignmentStatus.ACTIVE)
                .stream()
                .map(assignment -> assignment.getCatechistId())
                .collect(Collectors.toList());
    }

    @Override
    public int getStudentCount(Long classroomId, String academicYear) {
        return enrollmentRepository.countByClassroomIdAndAcademicYear(classroomId, academicYear);
    }

    @Override
    public int getCatechistCount(Long classroomId, String academicYear) {
        Integer count = assignmentRepository.countByClassroomIdAndAcademicYearAndStatus(
                classroomId, academicYear, AssignmentStatus.ACTIVE);
        return count != null ? count : 0;
    }

    @Override
    public List<StudentResponseDto> getStudentsInClassroom(Long classroomId, String academicYear) {
        classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + classroomId));
        
        // get student ids from enrollments
        List<Long> studentIds = enrollmentRepository.findByClassroomIdAndAcademicYear(classroomId, academicYear)
                .stream()
                .map(enrollment -> enrollment.getStudentId())
                .collect(Collectors.toList());
        
        if (studentIds.isEmpty()) {
            return List.of();
        }
        /*
         ApiResponse response = studentClient.getStudentsByIds(new HashSet<>(studentIds));
        List<Object> dataList = (List<Object>) response.getData();
        return dataList.stream()
                .map(data -> objectMapper.convertValue(data, StudentResponseDto.class))
                .collect(Collectors.toList());
        */
        //return studentClient.getStudentsByIds(new HashSet<>(studentIds));
        return studentClient.getStudentsByIds(studentIds);
    }

    @Override
    public List<CatechistResponseDto> getCatechistsInClassroom(Long classroomId, String academicYear) {
        // get catechist ids from assignments
        List<Long> catechistIds = assignmentRepository
                .findByClassroomIdAndAcademicYearAndStatus(classroomId, academicYear, AssignmentStatus.ACTIVE)
                .stream()
                .map(assignment -> assignment.getCatechistId())
                .collect(Collectors.toList());
        
        if (catechistIds.isEmpty()) {
            return List.of();
        }
        /*
        ApiResponse response = studentClient.getStudentsByIds(new HashSet<>(studentIds));
        List<Object> dataList = (List<Object>) response.getData();
        return dataList.stream()
                .map(data -> objectMapper.convertValue(data, StudentResponseDto.class))

                .collect(Collectors.toList());
        */
        //return catechistClient.getCatechistsByIds(new HashSet<>(catechistIds));
        return catechistClient.getCatechistsByIds(catechistIds);
    }
}
