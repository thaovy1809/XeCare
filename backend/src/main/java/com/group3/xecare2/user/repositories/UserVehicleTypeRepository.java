package com.group3.xecare2.user.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.group3.xecare2.user.entities.UserVehicleType;

public interface UserVehicleTypeRepository extends CrudRepository<UserVehicleType, Integer> {
	List<UserVehicleType> findAll();
}
