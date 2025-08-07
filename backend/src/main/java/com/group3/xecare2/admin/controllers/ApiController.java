package com.group3.xecare2.admin.controllers;


import com.group3.xecare2.admin.entities.*;
import com.group3.xecare2.admin.repositories.*;
import com.group3.xecare2.garage.dtos.MonthlyRevenueDto;
import com.group3.xecare2.garage.repositories.GarageRepository;
import com.group3.xecare2.user.dtos.ServiceDistributionDto;
import com.group3.xecare2.user.repositories.AppointmentRepository;
import com.group3.xecare2.user.repositories.AppointmentServiceRepository;
import com.group3.xecare2.user.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.group3.xecare2.enums.AppointmentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiController {

    @Autowired
    private ServiceRepository servicesRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AppointmentServiceRepository appointmentServiceRepository;

    @GetMapping("/revenue")
    public List<Map<String, Object>> getMonthlyRevenue(@RequestParam int year) {
        List<MonthlyRevenueDto> rawData = appointmentServiceRepository.getMonthlyRevenue(year);

        Map<Integer, BigDecimal> revenueMap = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            revenueMap.put(i, BigDecimal.ZERO);
        }

        for (MonthlyRevenueDto dto : rawData) {
            revenueMap.put(dto.getMonth(), dto.getTotalRevenue());
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("name", "T" + i);
            entry.put("value", revenueMap.get(i));
            result.add(entry);
        }

        return result;
    }

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private GarageRepository garageRepository;

    @GetMapping("/service-distribution")
    public List<ServiceDistributionDto> getServiceDistribution() {
        List<Object[]> rawData = appointmentServiceRepository.countServicesUsed();

        long total = rawData.stream()
                .mapToLong(row -> (Long) row[1])
                .sum();

        return rawData.stream().map(row -> {
            String name = (String) row[0];
            Long value = (Long) row[1];
            double percentage = total > 0 ? (value * 100.0 / total) : 0;

            return new ServiceDistributionDto(name, value, percentage);
        }).collect(Collectors.toList());
    }


    @GetMapping("/metrics")
    public Map<String, Object> getMetrics(@RequestParam String range) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from;

        switch (range) {
            case "7d" -> from = now.minusDays(7);
            case "30d" -> from = now.minusDays(30);
            case "90d" -> from = now.minusDays(90);
            case "1y" -> from = now.minusYears(1);
            default -> from = now.minusDays(30);
        }
        System.out.println(from);
        System.out.println(now);

        BigDecimal totalRevenue = appointmentRepository.getTotalRevenue(from, now);

        long totalAppointments = appointmentRepository.countByCreatedAtBetween(from, now);

        long completed = appointmentRepository.countByStatusAndCreatedAtBetween(AppointmentStatus.COMPLETED, from, now);
        long allInRange = appointmentRepository.countByCreatedAtBetween(from, now);
        double completionRate = allInRange > 0 ? (completed * 100.0 / allInRange) : 0;

        long activeGarages = garageRepository.countByCreatedAtBetween(from, now);

        Double avgRating = reviewRepository.getAverageRatingBetween(from, now);

        Map<String, Object> result = new HashMap<>();
        result.put("totalRevenue", totalRevenue);
        result.put("appointments", totalAppointments);
        result.put("completionRate", completionRate);
        result.put("activeGarages", activeGarages);
        result.put("averageRating", avgRating);
        return result;
    }

    @GetMapping("/booking-status")
    public ResponseEntity<List<Map<String, Object>>> getBookingStatusStats() {
        List<Object[]> rawStats = appointmentRepository.countAppointmentsByStatus();

        long total = rawStats.stream()
                .mapToLong(row -> (Long) row[1])
                .sum();

        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] row : rawStats) {
            AppointmentStatus status = (AppointmentStatus) row[0];
            long count = (Long) row[1];

            double percentage = total == 0 ? 0 : (count * 100.0) / total;

            String label;
            String color;
            switch (status) {
                case AppointmentStatus.COMPLETED -> {
                    label = "Hoàn thành";
                    color = "bg-green-500";
                }
                case AppointmentStatus.IN_PROGRESS -> {
                    label = "Đang xử lý";
                    color = "bg-blue-500";
                }
                case AppointmentStatus.PENDING -> {
                    label = "Chờ xác nhận";
                    color = "bg-yellow-500";
                }
                case AppointmentStatus.CANCELLED -> {
                    label = "Đã hủy";
                    color = "bg-red-500";
                }
                default -> {
                    label = "Khác";
                    color = "bg-gray-400";
                }
            }

            Map<String, Object> item = new HashMap<>();
            item.put("status", label);
            item.put("count", count);
            item.put("percentage", Math.round(percentage * 10.0) / 10.0);
            item.put("color", color);

            result.add(item);
        }

        return ResponseEntity.ok(result);
    }



    @GetMapping("/services")
    public List<Services> getActiveServices() {
        return servicesRepository.findByIsActiveTrue();
    }

    @GetMapping("/vehicle-types")
    public List<VehicleType> getActiveVehicleTypes() {
        return vehicleTypeRepository.findByIsActiveTrue();
    }
}


