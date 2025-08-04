package com.group3.xecare2.user.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.group3.xecare2.user.dtos.UserCreateDto;
import com.group3.xecare2.user.dtos.UserUpdateDto;
import com.group3.xecare2.user.entities.User;
import com.group3.xecare2.user.mappers.UserMapper;
import com.group3.xecare2.user.services.UserService;

@RestController
@RequestMapping({"/api/v1/users"})
@CrossOrigin(origins = "http://localhost:3000")
public class UserRestController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.listAll();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreateDto userCreateDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(userCreateDto.getPassword());
        userCreateDto.setPassword(encodedPassword);

        User user = new User();
        user.setName(userCreateDto.getName());
        user.setEmail(userCreateDto.getEmail());
        user.setPhone(userCreateDto.getPhone());
        user.setAddress(userCreateDto.getAddress());
        user.setPassword(userCreateDto.getPassword());

        User savedCustomer = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserInfo(
            @PathVariable("id") Integer id,
            @RequestBody UserUpdateDto dto
    ) {
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());

        User updated = userService.save(user);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<User> updateUserImage(
            @PathVariable("id") Integer id,
            @RequestPart("image") MultipartFile imageFile
    ) {
        try {
            User user = userService.findById(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            if (!imageFile.isEmpty()) {
                // Xóa ảnh cũ nếu có
                userService.deleteOldImage(user.getImageUrl());

                // Lưu ảnh mới
                String imageUrl = userService.saveImageToServer(imageFile);
                user.setImageUrl(imageUrl);
            }

            User updatedUser = userService.save(user);
            return ResponseEntity.ok(updatedUser);
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<User> toggleUserStatus(@PathVariable Integer id) {
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (user.getStatus() == null || user.getStatus() == 1) {
            user.setStatus(0); // Khóa
        } else {
            user.setStatus(1);
        }

        User updated = userService.save(user);
        return ResponseEntity.ok(updated);
    }

}
