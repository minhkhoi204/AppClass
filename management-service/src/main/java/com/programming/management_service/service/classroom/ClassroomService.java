package com.programming.management_service.service.classroom;

import com.programming.management_service.dto.ClassroomDto;
import com.programming.management_service.model.Classroom;

import java.util.List;

public interface ClassroomService {
    Classroom createClassroom(Classroom classroom);
    Classroom getClassroomById(Long id);
    List<Classroom> getAllClassrooms();
    Classroom updateClassroom(Long id, Classroom classroomDetails);
    void deleteClassroom(Long id);

    ClassroomDto getClassroomWithDetails(Long id);
}