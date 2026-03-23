package com.programming.management_service.repository;

import com.programming.management_service.domain.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    boolean existsByName(String name);

    List<Classroom> findByNameAndAcademicYear(String name, String academicYear);
}
