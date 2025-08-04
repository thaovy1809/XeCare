package com.group3.xecare2.user.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.group3.xecare2.garage.entities.Garage;
import com.group3.xecare2.user.entities.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
	private String name;
	 private String email;
	 private String password;
	 private LocalDateTime createdAt = LocalDateTime.now();
	 private String phone;
	 private String address;
}
