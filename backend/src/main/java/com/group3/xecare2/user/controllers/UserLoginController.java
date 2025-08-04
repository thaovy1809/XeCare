package com.group3.xecare2.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group3.xecare2.security.jwt.JwtUtils;
import com.group3.xecare2.user.dtos.SignInRequest;
import com.group3.xecare2.user.dtos.SignInResponse;
import com.group3.xecare2.user.entities.User;
import com.group3.xecare2.user.services.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/apis/v1")
public class UserLoginController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<?> SignIn(@RequestBody SignInRequest signInRequest) {
		try {
			System.out.println("Received login request for email: " + signInRequest.getEmail());
			User user = userService.findByEmail(signInRequest.getEmail());

			if (user == null) {
				System.out.println("Customer not found!");
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("email user not found");
			}

			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			boolean isLoginValid = encoder.matches(signInRequest.getPassword(), user.getPassword());

			if (!isLoginValid) {
				System.out.println("Password not matched!");
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("password not matched");
			}

			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
			String token = jwtUtils.generateJwtToken(authentication);

			SignInResponse response = new SignInResponse();
			response.setMessage("Login successful");
			response.setEmail(user.getEmail());
			response.setName(user.getName() );
			response.setTokenType("Bearer");
			response.setToken(token);
			response.setImageUrl(user.getImageUrl());
			response.setPhone(user.getPhone());
			response.setAddress(user.getAddress());
			response.setId(user.getId());
			response.setRole(user.getRole());
			response.setCreatedAt(user.getCreatedAt());
			System.out.println(response);

			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			e.printStackTrace(); // LOG FULL STACK TRACE
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong: " + e.getMessage());
		}
	}
}
