package com.group3.xecare2.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVehicleTypeResponseDto {
    private Integer id;
    private String brand;
    private String model;
    private String licensePlate;
    private Integer year;
    private String categoryName;
}
