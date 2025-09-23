package com.programming.user_service.service.classroom;

import com.programming.user_service.exceptions.ResourceNotFoundException;
import com.programming.user_service.model.Classroom;
import com.programming.user_service.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;

    @Override
    public Classroom createClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    @Override
    public Classroom getClassroomById(Long id) {
        //check
        return classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found for id " + id));
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
}
