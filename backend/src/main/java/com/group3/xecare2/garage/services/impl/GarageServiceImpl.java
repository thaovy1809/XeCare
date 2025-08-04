// Hoang
package com.group3.xecare2.garage.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group3.xecare2.admin.entities.VehicleType;
import com.group3.xecare2.admin.repositories.ServiceRepository;
import com.group3.xecare2.admin.repositories.VehicleTypeRepository;
import com.group3.xecare2.enums.GarageStatus;
import com.group3.xecare2.garage.dtos.GarageRegistrationDto;
import com.group3.xecare2.garage.dtos.GarageResponseDto;
import com.group3.xecare2.garage.dtos.GarageUpdateDto;
import com.group3.xecare2.garage.entities.Garage;
import com.group3.xecare2.garage.entities.GarageService;
import com.group3.xecare2.garage.entities.GarageVehicleType;
import com.group3.xecare2.garage.repositories.GarageRepository;
import com.group3.xecare2.garage.repositories.GarageServiceRepository;
import com.group3.xecare2.garage.repositories.GarageVehicleTypeRepository;
import com.group3.xecare2.garage.services.GarageServiceInterface;
import com.group3.xecare2.user.entities.User;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class GarageServiceImpl implements GarageServiceInterface {

    @Autowired
    private GarageRepository garageRepository;
    
    @Autowired
    private GarageServiceRepository garageServiceRepository;
    
    @Autowired
    private GarageVehicleTypeRepository garageVehicleTypeRepository;
    
    @Autowired
    private ServiceRepository serviceRepository;
    
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Override
    public GarageResponseDto registerGarage(GarageRegistrationDto registrationDto, User owner) {
        // Kiểm tra email và phone đã tồn tại chưa
        if (garageRepository.existsByEmail(registrationDto.getEmail())) {
            throw new IllegalArgumentException("Email đã được sử dụng bởi garage khác");
        }
        
        if (garageRepository.existsByPhone(registrationDto.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại đã được sử dụng bởi garage khác");
        }
        
        // Kiểm tra owner đã có garage chưa
        if (garageRepository.findByOwner(owner).isPresent()) {
            throw new IllegalArgumentException("Bạn đã có garage rồi");
        }
        
        // Tạo garage mới
        Garage garage = Garage.builder()
                .owner(owner)
                .name(registrationDto.getName())
                .description(registrationDto.getDescription())
                .address(registrationDto.getAddress())
                .phone(registrationDto.getPhone())
                .email(registrationDto.getEmail())
                .latitude(registrationDto.getLatitude())
                .longitude(registrationDto.getLongitude())
                .openTime(registrationDto.getOpenTime())
                .closeTime(registrationDto.getCloseTime())
                .status(GarageStatus.PENDING)
                .isVerified(false)
                .build();
        
        garage = garageRepository.save(garage);
        
        // Thêm các dịch vụ
        if (registrationDto.getServiceIds() != null) {
            for (Long serviceId : registrationDto.getServiceIds()) {
                com.group3.xecare2.admin.entities.Services service = serviceRepository.findById(serviceId)
                        .orElseThrow(() -> new EntityNotFoundException("Dịch vụ không tồn tại: " + serviceId));
                
                GarageService garageService = GarageService.builder()
                        .garage(garage)
                        .service(service)
                        .isActive(true)
                        .build();
                
                garageServiceRepository.save(garageService);
            }
        }
        
        // Thêm các loại xe hỗ trợ
        if (registrationDto.getVehicleTypeIds() != null) {
            for (Long vehicleTypeId : registrationDto.getVehicleTypeIds()) {
                VehicleType vehicleType = vehicleTypeRepository.findById(vehicleTypeId)
                        .orElseThrow(() -> new EntityNotFoundException("Loại xe không tồn tại: " + vehicleTypeId));
                
                GarageVehicleType garageVehicleType = GarageVehicleType.builder()
                        .garage(garage)
                        .vehicleType(vehicleType)
                        .isActive(true)
                        .build();
                
                garageVehicleTypeRepository.save(garageVehicleType);
            }
        }
        
        return convertToResponseDto(garage);
    }

    @Override
    public GarageResponseDto updateGarage(Long garageId, GarageUpdateDto updateDto, User owner) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new EntityNotFoundException("Garage không tồn tại"));
        
        // Kiểm tra quyền sở hữu
        if (!garage.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("Bạn không có quyền cập nhật garage này");
        }
        
        // Cập nhật thông tin cơ bản
        if (updateDto.getName() != null) {
            garage.setName(updateDto.getName());
        }
        if (updateDto.getDescription() != null) {
            garage.setDescription(updateDto.getDescription());
        }
        if (updateDto.getAddress() != null) {
            garage.setAddress(updateDto.getAddress());
        }
        if (updateDto.getPhone() != null) {
            // Kiểm tra phone mới có trùng không
            if (!updateDto.getPhone().equals(garage.getPhone()) && 
                garageRepository.existsByPhone(updateDto.getPhone())) {
                throw new IllegalArgumentException("Số điện thoại đã được sử dụng bởi garage khác");
            }
            garage.setPhone(updateDto.getPhone());
        }
        if (updateDto.getEmail() != null) {
            // Kiểm tra email mới có trùng không
            if (!updateDto.getEmail().equals(garage.getEmail()) && 
                garageRepository.existsByEmail(updateDto.getEmail())) {
                throw new IllegalArgumentException("Email đã được sử dụng bởi garage khác");
            }
            garage.setEmail(updateDto.getEmail());
        }
        if (updateDto.getLatitude() != null) {
            garage.setLatitude(updateDto.getLatitude());
        }
        if (updateDto.getLongitude() != null) {
            garage.setLongitude(updateDto.getLongitude());
        }
        if (updateDto.getOpenTime() != null) {
            garage.setOpenTime(updateDto.getOpenTime());
        }
        if (updateDto.getCloseTime() != null) {
            garage.setCloseTime(updateDto.getCloseTime());
        }
        if (updateDto.getImageUrl() != null) {
            garage.setImageUrl(updateDto.getImageUrl());
        }
        
        garage = garageRepository.save(garage);
        
        // Cập nhật dịch vụ nếu có
        if (updateDto.getServiceIds() != null) {
            // Xóa tất cả dịch vụ cũ
            List<GarageService> existingServices = garageServiceRepository.findByGarage(garage);
            garageServiceRepository.deleteAll(existingServices);
            
            // Thêm dịch vụ mới
            for (Long serviceId : updateDto.getServiceIds()) {
                com.group3.xecare2.admin.entities.Services service = serviceRepository.findById(serviceId)
                        .orElseThrow(() -> new EntityNotFoundException("Dịch vụ không tồn tại: " + serviceId));
                
                GarageService garageService = GarageService.builder()
                        .garage(garage)
                        .service(service)
                        .isActive(true)
                        .build();
                
                garageServiceRepository.save(garageService);
            }
        }
        
        // Cập nhật loại xe hỗ trợ nếu có
        if (updateDto.getVehicleTypeIds() != null) {
            // Xóa tất cả loại xe cũ
            List<GarageVehicleType> existingVehicleTypes = garageVehicleTypeRepository.findByGarage(garage);
            garageVehicleTypeRepository.deleteAll(existingVehicleTypes);
            
            // Thêm loại xe mới
            for (Long vehicleTypeId : updateDto.getVehicleTypeIds()) {
                VehicleType vehicleType = vehicleTypeRepository.findById(vehicleTypeId)
                        .orElseThrow(() -> new EntityNotFoundException("Loại xe không tồn tại: " + vehicleTypeId));
                
                GarageVehicleType garageVehicleType = GarageVehicleType.builder()
                        .garage(garage)
                        .vehicleType(vehicleType)
                        .isActive(true)
                        .build();
                
                garageVehicleTypeRepository.save(garageVehicleType);
            }
        }
        
        return convertToResponseDto(garage);
    }

    @Override
    public GarageResponseDto getGarageById(Long garageId) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new EntityNotFoundException("Garage không tồn tại"));
        return convertToResponseDto(garage);
    }

    @Override
    public GarageResponseDto getGarageByOwner(User owner) {
        Garage garage = garageRepository.findByOwner(owner)
                .orElseThrow(() -> new EntityNotFoundException("Bạn chưa có garage"));
        return convertToResponseDto(garage);
    }

    @Override
    public List<GarageResponseDto> getGaragesByStatus(String status) {
        GarageStatus garageStatus = GarageStatus.valueOf(status.toUpperCase());
        List<Garage> garages = garageRepository.findByStatus(garageStatus);
        return garages.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GarageResponseDto> searchGaragesByName(String name) {
        List<Garage> garages = garageRepository.findByNameContainingIgnoreCase(name);
        return garages.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GarageResponseDto> searchGaragesByAddress(String address) {
        List<Garage> garages = garageRepository.findByAddressContainingIgnoreCase(address);
        return garages.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GarageResponseDto> findNearbyGarages(Double latitude, Double longitude, Double radius) {
        List<Object[]> results = garageRepository.findNearbyGarages(latitude, longitude, radius);
        // TODO: Implement conversion from Object[] to GarageResponseDto
        return null; // Placeholder
    }

    @Override
    public void deleteGarage(Long garageId, User owner) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new EntityNotFoundException("Garage không tồn tại"));
        
        if (!garage.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("Bạn không có quyền xóa garage này");
        }
        
        garageRepository.delete(garage);
    }

    @Override
    public boolean existsByEmail(String email) {
        return garageRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return garageRepository.existsByPhone(phone);
    }

    @Override
    public Garage getGarageEntityById(Long garageId) {
        return garageRepository.findById(garageId)
                .orElseThrow(() -> new EntityNotFoundException("Garage không tồn tại"));
    }

    @Override
    public Garage getGarageEntityByOwner(User owner) {
        return garageRepository.findByOwner(owner)
                .orElseThrow(() -> new EntityNotFoundException("Bạn chưa có garage"));
    }
    
    private GarageResponseDto convertToResponseDto(Garage garage) {
        // Lấy danh sách dịch vụ
        List<GarageResponseDto.GarageServiceDto> services = garageServiceRepository.findByGarage(garage)
                .stream()
                .map(gs -> GarageResponseDto.GarageServiceDto.builder()
                        .id(gs.getId())
                        .serviceId(gs.getService().getId())
                        .serviceName(gs.getService().getName())
                        .serviceDescription(gs.getService().getDescription())
                        .basePrice(gs.getBasePrice() != null ? gs.getBasePrice().doubleValue() : null)
                        .estimatedTimeMinutes(gs.getEstimatedTimeMinutes())
                        .isActive(gs.getIsActive())
                        .build())
                .collect(Collectors.toList());
        
        // Lấy danh sách loại xe
        List<GarageResponseDto.GarageVehicleTypeDto> vehicleTypes = garageVehicleTypeRepository.findByGarage(garage)
                .stream()
                .map(gvt -> GarageResponseDto.GarageVehicleTypeDto.builder()
                        .id(gvt.getId())
                        .vehicleTypeId(gvt.getVehicleType().getId())
                        .vehicleTypeName(gvt.getVehicleType().getName())
                        .vehicleTypeDescription(gvt.getVehicleType().getDescription())
                        .isActive(gvt.getIsActive())
                        .build())
                .collect(Collectors.toList());
        
        return GarageResponseDto.builder()
                .id(garage.getId())
                .name(garage.getName())
                .description(garage.getDescription())
                .address(garage.getAddress())
                .phone(garage.getPhone())
                .email(garage.getEmail())
                .latitude(garage.getLatitude())
                .longitude(garage.getLongitude())
                .openTime(garage.getOpenTime())
                .closeTime(garage.getCloseTime())
                .imageUrl(garage.getImageUrl())
                .isVerified(garage.getIsVerified())
                .status(garage.getStatus())
                .createdAt(garage.getCreatedAt())
                .ownerId(garage.getOwner().getId())
                .ownerName(garage.getOwner().getName())
                .ownerEmail(garage.getOwner().getEmail())
                .services(services)
                .vehicleTypes(vehicleTypes)
                .build();
    }
} 