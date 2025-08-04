package com.group3.xecare2.garage.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.group3.xecare2.admin.entities.VehicleType;

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
@Table(name = "Garage_VehicleTypes", uniqueConstraints = @UniqueConstraint(columnNames = {"garage_id", "vehicle_type_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarageVehicleType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "garage_id")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private Garage garage;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private VehicleType vehicleType;
    
    private Boolean isActive = true;
}
