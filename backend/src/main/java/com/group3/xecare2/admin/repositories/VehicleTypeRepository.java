package com.group3.xecare2.admin.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group3.xecare2.admin.entities.VehicleType;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {
    
    // Tìm loại xe theo tên
    Optional<VehicleType> findByName(String name);
    
    // Tìm loại xe đang hoạt động
    List<VehicleType> findByIsActiveTrue();
    
    // Tìm loại xe theo tên (tìm kiếm mờ)
    List<VehicleType> findByNameContainingIgnoreCase(String name);
    
    // Kiểm tra loại xe có tồn tại không
    boolean existsByName(String name);
} 