// Hoang
package com.group3.xecare2.garage.dtos;

import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarageUpdateDto {
    
    @Size(min = 2, max = 100, message = "Tên garage phải từ 2-100 ký tự")
    private String name;
    
    @Size(max = 500, message = "Mô tả không được quá 500 ký tự")
    private String description;
    
    @Size(max = 255, message = "Địa chỉ không được quá 255 ký tự")
    private String address;
    
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại không hợp lệ")
    private String phone;
    
    @Email(message = "Email không hợp lệ")
    private String email;
    
    private Double latitude;
    private Double longitude;
    
    private LocalTime openTime;
    private LocalTime closeTime;
    
    private String imageUrl;
    
    // Danh sách ID của các dịch vụ
    private List<Long> serviceIds;
    
    // Danh sách ID của các loại xe hỗ trợ
    private List<Long> vehicleTypeIds;
} 