// Hoang
package com.group3.xecare2.garage.controllers;

import com.group3.xecare2.garage.dtos.GarageResponseDto;
import com.group3.xecare2.garage.entities.Garage;
import com.group3.xecare2.garage.services.GarageServiceInterface;
import com.group3.xecare2.security.AppUserDetails;
import com.group3.xecare2.user.entities.User;
import com.group3.xecare2.user.repositories.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.group3.xecare2.user.dtos.ReviewDto;
import com.group3.xecare2.user.entities.Review;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reviews")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    // Helper chuyển Review -> ReviewDto
    private ReviewDto toDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .appointmentId(review.getAppointment().getId())
                .userId(review.getUser().getId())
                .userName(review.getUser().getName()) // giả sử user có getName()
                .garageId(review.getGarage() != null ? review.getGarage().getId() : null)
                .garageName(review.getGarage() != null ? review.getGarage().getName() : null)
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .status(review.getStatus())
                .build();
    }

    @GetMapping
    public List<ReviewDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/set-status")
    public ResponseEntity<ReviewDto> toggleReviewStatus(@PathVariable Integer id, @RequestBody Map<String, Integer> body) {
        Integer status = body.get("status");
        return reviewRepository.findById(id).map(review -> {
            review.setStatus(status);
            Review updated = reviewRepository.save(review);
            return ResponseEntity.ok(toDto(updated));
        }).orElse(ResponseEntity.notFound().build());
    }
}
