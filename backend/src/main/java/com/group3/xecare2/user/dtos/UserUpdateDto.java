package com.group3.xecare2.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

	private String name;
	private String email;
	private String phone;
	private String address;

}
