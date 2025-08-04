package com.group3.xecare2.user.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.group3.xecare2.enums.AccountType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse implements Serializable {

	private static final long serialVersionUID = -4197103061331638647L;
	
	private Integer id;
	private String token;
	private String refreshToken;
	private String tokenType = "Bearer";
	private String name;
	private String email;
	private String Message;
	private String imageUrl;
	private String phone;
	private String address;
	private AccountType role;
	private LocalDateTime createdAt;
}
