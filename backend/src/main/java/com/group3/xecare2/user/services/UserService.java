package com.group3.xecare2.user.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.group3.xecare2.user.entities.User;

public interface UserService {

	User findByEmail(String username);

	List<User> listAll();

	
	User findById(Integer id);
	User save(User user);
	
	String saveImageToServer(MultipartFile imageFile) throws IOException;
	void deleteOldImage(String imagePath);

}
