package com.group3.xecare2.user.repositories;

import org.springframework.data.repository.CrudRepository;

import com.group3.xecare2.user.entities.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	User findByEmail(String email);
}
