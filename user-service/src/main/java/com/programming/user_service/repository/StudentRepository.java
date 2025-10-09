package com.programming.user_service.repository;

import com.programming.user_service.domain.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
//    Optional<Student> findByUserId(Long userId);
//
//    List<Student> findAllByClassroomId(Long classroomId);
//
//    boolean existsByUserId(Long userId); // createStudent
//    boolean existsByUserFullNameAndSaintName(String fullName, String saintName);

    //List<Student> findByFullNameIgnoreCase(String fullName);

}