package com.programming.management_service.repository;

import com.programming.management_service.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    boolean existsByName(String name);
}
