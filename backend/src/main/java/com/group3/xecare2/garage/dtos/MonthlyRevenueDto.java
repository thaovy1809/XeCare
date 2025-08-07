package com.group3.xecare2.garage.dtos;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyRevenueDto {
    private Integer month;
    private BigDecimal totalRevenue;
}
