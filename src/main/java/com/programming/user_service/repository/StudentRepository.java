package com.programming.user_service.repository;

import com.programming.user_service.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByClassroomId(Long classroomId);
    boolean existsByUserId(Long userId); // như bạn đã dùng ở createStudent
}