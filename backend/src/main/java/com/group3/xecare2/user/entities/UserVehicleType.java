package com.group3.xecare2.user.entities;

import com.group3.xecare2.admin.entities.VehicleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "User_VehicleTypes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVehicleType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private UserVehicleTypeCategory category;

	@ManyToOne
    @JoinColumn(name = "vehicle_type_id", nullable = false) // thêm dòng này
    private VehicleType vehicleType;
	
	private String brand;
	private String model;

	@Column(name = "license_plate", length = 50)
	private String licensePlate;

	private Integer year;
}
