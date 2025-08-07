package com.group3.xecare2.garage.entities;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.group3.xecare2.admin.entities.Services;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

@Entity
@Table(name = "Garage_Services", uniqueConstraints = @UniqueConstraint(columnNames = {"garage_id", "service_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarageService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "garage_id")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Garage garage;

    @ManyToOne
    @JoinColumn(name = "service_id")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Services service;

    private BigDecimal basePrice;
    private Integer estimatedTimeMinutes;
    private Boolean isActive = true;
    
    //thêm thuộc tính serviceType để phân loại dịch vụ theo loại
    @ManyToOne
    @JoinColumn(name = "service_type_id") // cột FK trong bảng Garage_Services
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private GarageServiceType garageServiceType;
    
}