// Hoang
package com.group3.xecare2.garage.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.group3.xecare2.user.repositories.ReviewRepository;
import com.group3.xecare2.user.entities.Review;
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
    public GarageResponseDto registerGarage(GarageRegistrationDto registrationDto) {
        // Kiểm tra email và phone đã tồn tại chưa
        if (garageRepository.existsByEmail(registrationDto.getEmail())) {
            throw new IllegalArgumentException("Email đã được sử dụng bởi garage khác");
        }

        if (garageRepository.existsByPhone(registrationDto.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại đã được sử dụng bởi garage khác");
        }

        // Tạo garage mới
        Garage garage = Garage.builder()
                .name(registrationDto.getName())
                .description(registrationDto.getDescription())
                .address(registrationDto.getAddress())
                .phone(registrationDto.getPhone())
                .email(registrationDto.getEmail())
                .latitude(registrationDto.getLatitude())
                .longitude(registrationDto.getLongitude())
                .openTime(registrationDto.getOpenTime())
                .closeTime(registrationDto.getCloseTime())
                .status(GarageStatus.ACTIVE)
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

    private boolean areListsEqualIgnoreOrder(List<Long> list1, List<Long> list2) {
        if (list1 == null || list2 == null) return false;
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }


    @Override
    public GarageResponseDto updateGarage(Long garageId, GarageUpdateDto updateDto) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new EntityNotFoundException("Garage không tồn tại"));


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
            List<Long> currentServiceIds = garageServiceRepository.findByGarage(garage).stream()
                    .map(gs -> gs.getService().getId())
                    .collect(Collectors.toList());

            if (!areListsEqualIgnoreOrder(updateDto.getServiceIds(), currentServiceIds)) {
                // Xóa và thêm mới nếu có sự thay đổi
                garageServiceRepository.deleteAllByGarage(garage);
                garageServiceRepository.flush();
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
        }


        // Cập nhật loại xe hỗ trợ nếu có
        if (updateDto.getVehicleTypeIds() != null) {
            List<Long> currentVehicleTypeIds = garageVehicleTypeRepository.findByGarage(garage).stream()
                    .map(gvt -> gvt.getVehicleType().getId())
                    .collect(Collectors.toList());

            if (!areListsEqualIgnoreOrder(updateDto.getVehicleTypeIds(), currentVehicleTypeIds)) {
                garageVehicleTypeRepository.deleteAllByGarage(garage);
                garageVehicleTypeRepository.flush();
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
        }


        return convertToResponseDto(garage);
    }

    @Override
    public GarageResponseDto getGarageById(Long garageId) {
        System.out.println(garageId);
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new EntityNotFoundException("Garage không tồn tại"));
        System.out.println(garage);
        return convertToResponseDto(garage);
    }

    @Override
    public List<GarageResponseDto> findAll() {
        List<Garage> garages = garageRepository.findAll();
        List<GarageResponseDto> responseDtos = new ArrayList<GarageResponseDto>();

        for (Garage garage : garages) {
            GarageResponseDto dto = convertToResponseDto(garage);
            responseDtos.add(dto);
        }

        return responseDtos;
    }

    @Override
    public Garage saveGarage(Garage garage) {
        return garageRepository.save(garage);
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

    @Autowired
    private ReviewRepository reviewRepository;

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

        List<Review> reviews = reviewRepository.findByGarage(garage);

        double averageRating = 0;
        int reviewCount = reviews.size();

        if (reviewCount > 0) {
            averageRating = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
        }
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
                .services(services)
                .vehicleTypes(vehicleTypes)
                .averageRating(averageRating)
                .reviewCount(reviewCount)
                .build();
    }
} 