// Hoang
package com.group3.xecare2.garage.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group3.xecare2.admin.entities.Services;
import com.group3.xecare2.garage.entities.Garage;
import com.group3.xecare2.garage.entities.GarageService;

@Repository
public interface GarageServiceRepository extends JpaRepository<GarageService, Long> {
    
    // Tìm tất cả dịch vụ của một garage
    List<GarageService> findByGarage(Garage garage);
    
    // Tìm dịch vụ đang hoạt động của một garage
    List<GarageService> findByGarageAndIsActiveTrue(Garage garage);
    
    // Tìm dịch vụ cụ thể của một garage
    Optional<GarageService> findByGarageAndService(Garage garage, Services service);
    
    // Tìm tất cả garage cung cấp một dịch vụ cụ thể
    List<GarageService> findByServiceAndIsActiveTrue(Services service);
    
    // Kiểm tra garage đã có dịch vụ này chưa
    boolean existsByGarageAndService(Garage garage, Services service);

    void deleteAllByGarage(Garage garage);
} 