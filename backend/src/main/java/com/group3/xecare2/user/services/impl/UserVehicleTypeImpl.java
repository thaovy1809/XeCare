package com.group3.xecare2.user.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.xecare2.user.dtos.UserVehicleTypeCreateDto;
import com.group3.xecare2.user.dtos.UserVehicleTypeResponseDto;
import com.group3.xecare2.user.entities.User;
import com.group3.xecare2.user.entities.UserVehicleType;
import com.group3.xecare2.user.entities.UserVehicleTypeCategory;
import com.group3.xecare2.user.repositories.UserVehicleTypeCategoryRepository;
import com.group3.xecare2.user.repositories.UserVehicleTypeRepository;
import com.group3.xecare2.user.services.UserVehicleTypeService;

@Service
public class UserVehicleTypeImpl implements UserVehicleTypeService {

	@Autowired
	private UserVehicleTypeRepository userVehicleTypeRepository;

	@Autowired
	private UserVehicleTypeCategoryRepository categoryRepository;

	@Override
	public List<UserVehicleType> listAll() {
		// TODO Auto-generated method stub
		return (List<UserVehicleType>) userVehicleTypeRepository.findAll();
	}
	@Override
	public List<UserVehicleTypeResponseDto> getAllVehicleTypes() {
	    List<UserVehicleType> entities = userVehicleTypeRepository.findAll();

	    return entities.stream().map(vehicle -> UserVehicleTypeResponseDto.builder()
	            .id(vehicle.getId())
	            .brand(vehicle.getBrand())
	            .model(vehicle.getModel())
	            .licensePlate(vehicle.getLicensePlate())
	            .year(vehicle.getYear())
	            .categoryName(vehicle.getCategory().getName())
	            .build()
	    ).toList();
	}

	@Override
	public UserVehicleTypeResponseDto createUserVehicle(UserVehicleTypeCreateDto dto, User currentUser) {
	    UserVehicleTypeCategory category = categoryRepository.findById(dto.getCategoryId())
	        .orElseThrow(() -> new IllegalArgumentException("Category không tồn tại"));

	    UserVehicleType vehicle = UserVehicleType.builder()
	        .brand(dto.getBrand())
	        .model(dto.getModel())
	        .licensePlate(dto.getLicensePlate())
	        .year(dto.getYear())
	        .category(category)
	        .user(currentUser)
	        .build();

	    vehicle = userVehicleTypeRepository.save(vehicle);

	    return UserVehicleTypeResponseDto.builder()
	        .id(vehicle.getId())
	        .brand(vehicle.getBrand())
	        .model(vehicle.getModel())
	        .licensePlate(vehicle.getLicensePlate())
	        .year(vehicle.getYear())
	        .categoryName(category.getName())
	        .build();
	}

}
