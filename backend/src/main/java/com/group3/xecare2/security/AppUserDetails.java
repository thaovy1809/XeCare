package com.group3.xecare2.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.group3.xecare2.user.entities.User;


public class AppUserDetails implements UserDetails {

	private static final long serialVersionUID = -8498532525619268861L;

	private User user;
	
	public AppUserDetails(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> listAuthorities = new ArrayList<>();
		
		listAuthorities.add(new SimpleGrantedAuthority("USER"));
		return listAuthorities;
	}

	@Override
	public String getPassword() {
		
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}
	public List<String> roles(){
		List<String> roles = new ArrayList<>();
		roles.add("user");
		return roles;
	}

	
}
