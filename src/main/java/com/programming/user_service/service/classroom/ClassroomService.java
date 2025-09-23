package com.programming.user_service.service.classroom;

import com.programming.user_service.model.Classroom;

import java.util.List;
import java.util.Optional;

public interface ClassroomService {
    Classroom createClassroom(Classroom classroom);
    Classroom getClassroomById(Long id);
    List<Classroom> getAllClassrooms();
    Classroom updateClassroom(Long id, Classroom classroomDetails);
    void deleteClassroom(Long id);
}
