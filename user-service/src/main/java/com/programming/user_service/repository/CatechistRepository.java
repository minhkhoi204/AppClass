package com.programming.user_service.repository;

import com.programming.user_service.domain.model.Catechist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatechistRepository extends JpaRepository<Catechist, Long> {
    boolean existsByUserFullNameAndUserChristianName(String fullName, String christianName);
    boolean existsByUserId(Long userId);

//    Optional<Student> findByUserId(Long userId);
//
//    List<Student> findAllByClassroomId(Long classroomId);
//
//    boolean existsByUserId(Long userId); // createStudent
//    boolean existsByUserFullNameAndChristianName(String fullName, String christianName);

    //List<Student> findByFullNameIgnoreCase(String fullName);

}