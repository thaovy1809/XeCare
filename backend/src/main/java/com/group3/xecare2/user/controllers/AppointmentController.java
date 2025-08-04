package com.group3.xecare2.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group3.xecare2.security.AppUserDetails;
import com.group3.xecare2.user.dtos.AppointmentRequest;
import com.group3.xecare2.user.entities.Appointment;
import com.group3.xecare2.user.entities.User;
import com.group3.xecare2.user.services.AppointmentServiceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/apis/v1/appointments")
public class AppointmentController {

	@Autowired
    private AppointmentServiceService appointmentService;

    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        try {
            // Lấy user đang đăng nhập từ context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
            User currentUser = userDetails.getUser();

            // Gán userId vào request từ người dùng hiện tại
            request.setUserId(currentUser.getId());

            Appointment response = appointmentService.createAppointment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống");
        }
    }
}
