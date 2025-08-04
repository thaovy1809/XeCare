// Hoang
package com.group3.xecare2.garage.dtos;

import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class GarageRegistrationDto {
    
    @NotBlank(message = "Tên garage không được để trống")
    @Size(min = 2, max = 100, message = "Tên garage phải từ 2-100 ký tự")
    private String name;
    
    @Size(max = 500, message = "Mô tả không được quá 500 ký tự")
    private String description;
    
    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 255, message = "Địa chỉ không được quá 255 ký tự")
    private String address;
    
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại không hợp lệ")
    private String phone;
    
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;
    
    @NotNull(message = "Vĩ độ không được để trống")
    private Double latitude;
    
    @NotNull(message = "Kinh độ không được để trống")
    private Double longitude;
    
    private LocalTime openTime;
    private LocalTime closeTime;
    
    // Danh sách ID của các dịch vụ
    @NotNull(message = "Danh sách dịch vụ không được để trống")
    @Size(min = 1, message = "Phải chọn ít nhất 1 dịch vụ")
    private List<Long> serviceIds;
    
    // Danh sách ID của các loại xe hỗ trợ
    @NotNull(message = "Danh sách loại xe không được để trống")
    @Size(min = 1, message = "Phải chọn ít nhất 1 loại xe")
    private List<Long> vehicleTypeIds;
} 