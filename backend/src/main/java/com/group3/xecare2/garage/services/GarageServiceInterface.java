// Hoang
package com.group3.xecare2.garage.services;

import java.util.List;

import com.group3.xecare2.garage.dtos.GarageRegistrationDto;
import com.group3.xecare2.garage.dtos.GarageResponseDto;
import com.group3.xecare2.garage.dtos.GarageUpdateDto;
import com.group3.xecare2.garage.entities.Garage;
import com.group3.xecare2.user.entities.User;

public interface GarageServiceInterface {
    
    // Đăng ký garage mới
    GarageResponseDto registerGarage(GarageRegistrationDto registrationDto, User owner);
    
    // Cập nhật thông tin garage
    GarageResponseDto updateGarage(Long garageId, GarageUpdateDto updateDto, User owner);
    
    // Lấy thông tin garage theo ID
    GarageResponseDto getGarageById(Long garageId);
    
    // Lấy thông tin garage của chủ sở hữu
    GarageResponseDto getGarageByOwner(User owner);
    
    // Lấy danh sách garage theo trạng thái
    List<GarageResponseDto> getGaragesByStatus(String status);
    
    // Tìm garage theo tên
    List<GarageResponseDto> searchGaragesByName(String name);
    
    // Tìm garage theo địa chỉ
    List<GarageResponseDto> searchGaragesByAddress(String address);
    
    // Tìm garage gần nhất
    List<GarageResponseDto> findNearbyGarages(Double latitude, Double longitude, Double radius);
    
    // Xóa garage
    void deleteGarage(Long garageId, User owner);
    
    // Kiểm tra garage có tồn tại không
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);
    
    // Lấy entity Garage theo ID
    Garage getGarageEntityById(Long garageId);
    
    // Lấy entity Garage theo chủ sở hữu
    Garage getGarageEntityByOwner(User owner);
} 