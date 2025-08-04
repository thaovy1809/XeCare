package com.group3.xecare2.user.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class AppointmentRequest {
    private Integer userId;
    private Long garageId;
    private Long vehicleTypeId;
    private String description;
    private String imageUrl;
    private LocalDateTime appointmentTime;
    private String notes;
    private List<ServiceItem> services;

    @Data
    public static class ServiceItem {
        private Long serviceId;
        private String price;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}