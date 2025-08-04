package com.group3.xecare2.user.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest implements Serializable {

	private static final long serialVersionUID = 6549159968578871819L;

	private String email;

	private String password;
}
