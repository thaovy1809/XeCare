package com.group3.xecare2.admin.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group3.xecare2.admin.entities.Services;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {
    
    // Tìm dịch vụ theo tên
    Optional<Services> findByName(String name);
    
    // Tìm dịch vụ đang hoạt động
    List<Services> findByIsActiveTrue();
    
    // Tìm dịch vụ theo tên (tìm kiếm mờ)
    List<Services> findByNameContainingIgnoreCase(String name);
    
    // Kiểm tra dịch vụ có tồn tại không
    boolean existsByName(String name);
} 