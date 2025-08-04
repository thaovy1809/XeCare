// Hoang
package com.group3.xecare2.garage.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group3.xecare2.garage.dtos.GarageRegistrationDto;
import com.group3.xecare2.garage.dtos.GarageResponseDto;
import com.group3.xecare2.garage.dtos.GarageUpdateDto;
import com.group3.xecare2.garage.services.GarageServiceInterface;
import com.group3.xecare2.security.AppUserDetails;
import com.group3.xecare2.user.entities.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/apis/garage")
public class GarageController {

    @Autowired
    private GarageServiceInterface garageService;

    /**
     * Đăng ký garage mới
     * POST /api/garage/register
     */
    @PostMapping("/register")
    public ResponseEntity<GarageResponseDto> registerGarage(@Valid @RequestBody GarageRegistrationDto registrationDto) {
        try {
            // Lấy thông tin user hiện tại từ security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
            User currentUser = userDetails.getUser();
            
            GarageResponseDto response = garageService.registerGarage(registrationDto, currentUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // Trả về lỗi validation
        	 e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Trả về lỗi server
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cập nhật thông tin garage
     * PUT /api/garage/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<GarageResponseDto> updateGarage(@PathVariable("id") Long id, 
                                                         @Valid @RequestBody GarageUpdateDto updateDto) {
        try {
            // Lấy thông tin user hiện tại từ security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
            User currentUser = userDetails.getUser();
            
            GarageResponseDto response = garageService.updateGarage(id, updateDto, currentUser);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Trả về lỗi validation hoặc quyền truy cập
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Trả về lỗi server
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lấy thông tin garage theo ID
     * GET /api/garage/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<GarageResponseDto> getGarageById(@PathVariable("id") Long id) {
        try {
            GarageResponseDto response = garageService.getGarageById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Lấy thông tin garage của user hiện tại
     * GET /api/garage/my-garage
     */
    @GetMapping("/my-garage")
    public ResponseEntity<GarageResponseDto> getMyGarage() {
        try {
            // Lấy thông tin user hiện tại từ security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
            User currentUser = userDetails.getUser();
            
            GarageResponseDto response = garageService.getGarageByOwner(currentUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Lấy danh sách garage theo trạng thái
     * GET /api/garage/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<GarageResponseDto>> getGaragesByStatus(@PathVariable("status") String status) {
        try {
            List<GarageResponseDto> response = garageService.getGaragesByStatus(status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Tìm garage theo tên
     * GET /api/garage/search/name?name={name}
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<GarageResponseDto>> searchGaragesByName(@RequestParam String name) {
        try {
            List<GarageResponseDto> response = garageService.searchGaragesByName(name);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Tìm garage theo địa chỉ
     * GET /api/garage/search/address?address={address}
     */
    @GetMapping("/search/address")
    public ResponseEntity<List<GarageResponseDto>> searchGaragesByAddress(@RequestParam String address) {
        try {
            List<GarageResponseDto> response = garageService.searchGaragesByAddress(address);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Tìm garage gần nhất
     * GET /api/garage/nearby?latitude={lat}&longitude={lng}&radius={radius}
     */
    @GetMapping("/nearby")
    public ResponseEntity<List<GarageResponseDto>> findNearbyGarages(@RequestParam Double latitude,
                                                                    @RequestParam Double longitude,
                                                                    @RequestParam(defaultValue = "10.0") Double radius) {
        try {
            List<GarageResponseDto> response = garageService.findNearbyGarages(latitude, longitude, radius);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Xóa garage
     * DELETE /api/garage/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGarage(@PathVariable("id") Long id) {
        try {
            // Lấy thông tin user hiện tại từ security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
            User currentUser = userDetails.getUser();
            
            garageService.deleteGarage(id, currentUser);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Kiểm tra email đã tồn tại chưa
     * GET /api/garage/check-email?email={email}
     */
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        try {
            boolean exists = garageService.existsByEmail(email);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Kiểm tra số điện thoại đã tồn tại chưa
     * GET /api/garage/check-phone?phone={phone}
     */
    @GetMapping("/check-phone")
    public ResponseEntity<Boolean> checkPhoneExists(@RequestParam String phone) {
        try {
            boolean exists = garageService.existsByPhone(phone);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 