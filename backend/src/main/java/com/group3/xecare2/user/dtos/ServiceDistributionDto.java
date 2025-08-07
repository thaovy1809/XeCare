package com.group3.xecare2.user.dtos;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDistributionDto {
    private String name;
    private Long value;        // Số lượt dùng
    private Double percentage; // Tỷ lệ %
}
