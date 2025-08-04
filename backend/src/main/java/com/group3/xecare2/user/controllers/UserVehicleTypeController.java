package com.group3.xecare2.user.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group3.xecare2.security.AppUserDetails;
import com.group3.xecare2.user.dtos.UserVehicleTypeCreateDto;
import com.group3.xecare2.user.dtos.UserVehicleTypeResponseDto;
import com.group3.xecare2.user.entities.User;
import com.group3.xecare2.user.entities.UserVehicleType;
import com.group3.xecare2.user.services.UserService;
import com.group3.xecare2.user.services.UserVehicleTypeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({ "/apis/user/Vehicle" })
public class UserVehicleTypeController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserVehicleTypeService userVehicleTypeService;

//	@GetMapping
//	public List<UserVehicleTypeResponseDto> getAllUserVehicleType() {
//		return userVehicleTypeService.listAll();
//	}
	@GetMapping
	public ResponseEntity<List<UserVehicleTypeResponseDto>> getAllUserVehicleType() {
	    List<UserVehicleTypeResponseDto> response = userVehicleTypeService.getAllVehicleTypes();
	    return ResponseEntity.ok(response);
	}

	@PostMapping("/create")
	public ResponseEntity<?> createUserVehicleType(
	        @Valid @RequestBody UserVehicleTypeCreateDto dto) {
	    try {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
	        User currentUser = userDetails.getUser();

	        UserVehicleTypeResponseDto response = userVehicleTypeService.createUserVehicle(dto, currentUser);
	        return ResponseEntity.status(HttpStatus.CREATED).body(response);

	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
	                "Error: " + e.getMessage());
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
	                "Internal Server Error");
	    }
	}
}
