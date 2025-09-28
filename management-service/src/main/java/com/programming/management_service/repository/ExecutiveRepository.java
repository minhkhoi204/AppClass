package com.programming.management_service.repository;

import com.programming.management_service.model.Executive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutiveRepository extends JpaRepository<Executive, Long> {
    // Bạn có thể thêm custom query ở đây nếu cần
}
