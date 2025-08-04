package com.group3.xecare2.user.services;

import java.util.List;

import com.group3.xecare2.user.dtos.UserVehicleTypeCreateDto;
import com.group3.xecare2.user.dtos.UserVehicleTypeResponseDto;
import com.group3.xecare2.user.entities.User;
import com.group3.xecare2.user.entities.UserVehicleType;

public interface UserVehicleTypeService {

	List<UserVehicleType> listAll();
	List<UserVehicleTypeResponseDto> getAllVehicleTypes();
    UserVehicleTypeResponseDto createUserVehicle(UserVehicleTypeCreateDto dto, User currentUser);
}
