package com.group3.xecare2.user.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.group3.xecare2.user.entities.User;
import com.group3.xecare2.user.repositories.UserRepository;
import com.group3.xecare2.user.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User findByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}

	@Override
	public List<User> listAll() {
		// TODO Auto-generated method stub
		return (List<User>) userRepository.findAll();
	}

	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}

	@Override
	public User findById(Integer id) {
	    return userRepository.findById(id).orElse(null);
	}

	@Override
	public String saveImageToServer(MultipartFile imageFile) throws IOException {
	    if (imageFile != null && !imageFile.isEmpty()) {
	        // Kiểm tra loại tệp
	        String contentType = imageFile.getContentType();
	        if (contentType == null || !contentType.startsWith("image/")) {
	            throw new IllegalArgumentException("File must be an image.");
	        }

	        // Kiểm tra kích thước (<= 5MB)
	        long maxSize = 5 * 1024 * 1024;
	        if (imageFile.getSize() > maxSize) {
	            throw new IllegalArgumentException("File size exceeds 5MB.");
	        }

	        // Tên file
	        String originalName = imageFile.getOriginalFilename();
	        String fileExtension = originalName.substring(originalName.lastIndexOf("."));
	        String baseName = originalName.substring(0, originalName.lastIndexOf("."));

	        // Đường dẫn thư mục
	        String uploadDir = new File("uploads").getAbsolutePath();
	        File directory = new File(uploadDir);
	        if (!directory.exists()) {
	            directory.mkdirs();
	        }

	        // Tạo tên file tránh trùng lặp
	        String fileName = originalName;
	        File destFile = new File(directory, fileName);
	        int counter = 1;
	        while (destFile.exists()) {
	            fileName = baseName + "_" + counter + fileExtension;
	            destFile = new File(directory, fileName);
	            counter++;
	        }

	        // Lưu file
	        imageFile.transferTo(destFile);

	        return "uploads/" + fileName; // dùng để set trong `user.setImageUrl(...)`
	    }
	    return "";
	}

	@Override
	public void deleteOldImage(String imagePath) {
	    if (imagePath != null && !imagePath.isEmpty()) {
	        File file = new File(new File("").getAbsolutePath(), imagePath);
	        if (file.exists()) {
	            file.delete();
	            System.out.println("Deleted old image: " + file.getAbsolutePath());
	        } else {
	            System.out.println("Old image not found: " + file.getAbsolutePath());
	        }
	    }
	}
	

}
