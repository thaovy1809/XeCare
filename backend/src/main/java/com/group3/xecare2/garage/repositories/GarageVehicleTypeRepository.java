// Hoang
package com.group3.xecare2.garage.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group3.xecare2.admin.entities.VehicleType;
import com.group3.xecare2.garage.entities.Garage;
import com.group3.xecare2.garage.entities.GarageVehicleType;

@Repository
public interface GarageVehicleTypeRepository extends JpaRepository<GarageVehicleType, Long> {
    
    // Tìm tất cả loại xe mà garage hỗ trợ
    List<GarageVehicleType> findByGarage(Garage garage);
    
    // Tìm loại xe đang hoạt động mà garage hỗ trợ
    List<GarageVehicleType> findByGarageAndIsActiveTrue(Garage garage);
    
    // Tìm garage hỗ trợ một loại xe cụ thể
    List<GarageVehicleType> findByVehicleTypeAndIsActiveTrue(VehicleType vehicleType);
    
    // Tìm loại xe cụ thể của một garage
    Optional<GarageVehicleType> findByGarageAndVehicleType(Garage garage, VehicleType vehicleType);
    
    // Kiểm tra garage đã hỗ trợ loại xe này chưa
    boolean existsByGarageAndVehicleType(Garage garage, VehicleType vehicleType);
} 