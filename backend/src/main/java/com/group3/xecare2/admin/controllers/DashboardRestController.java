package com.group3.xecare2.admin.controllers;


import com.group3.xecare2.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.group3.xecare2.garage.repositories.*;
import com.group3.xecare2.user.repositories.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:3000")
public class DashboardRestController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GarageRepository garageRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private ReviewRepository reviewRepository;


    @GetMapping
    public ResponseEntity<?> getDashboardStats() {
        long totalUsers = userRepository.count();

        long activeGarages = garageRepository.countByIsVerifiedTrue();

        LocalDate today = LocalDate.now();
        long todayAppointments = appointmentRepository.countByDate(today);

        Double avgRating = reviewRepository.findAverageRating();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("activeGarages", activeGarages);
        stats.put("todayAppointments", todayAppointments);
        stats.put("averageRating", avgRating != null ? avgRating : 0);

        return ResponseEntity.ok(stats);
    }
}

