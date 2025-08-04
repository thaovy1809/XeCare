// Hoang
package com.group3.xecare2.garage.dtos;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.group3.xecare2.enums.GarageStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarageResponseDto {
    
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private String email;
    private Double latitude;
    private Double longitude;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String imageUrl;
    private Boolean isVerified;
    private GarageStatus status;
    private LocalDateTime createdAt;
    
    // Thông tin chủ sở hữu
    private Integer ownerId;
    private String ownerName;
    private String ownerEmail;
    
    // Danh sách dịch vụ
    private List<GarageServiceDto> services;
    
    // Danh sách loại xe hỗ trợ
    private List<GarageVehicleTypeDto> vehicleTypes;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GarageServiceDto {
        private Long id;
        private Long serviceId;
        private String serviceName;
        private String serviceDescription;
        private Double basePrice;
        private Integer estimatedTimeMinutes;
        private Boolean isActive;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GarageVehicleTypeDto {
        private Long id;
        private Long vehicleTypeId;
        private String vehicleTypeName;
        private String vehicleTypeDescription;
        private Boolean isActive;
    }
} 