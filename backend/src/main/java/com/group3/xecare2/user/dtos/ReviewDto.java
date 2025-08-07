package com.group3.xecare2.user.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Integer id;
    private Integer appointmentId;
    private Integer userId;
    private String userName;
    private Long garageId;
    private String garageName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private Integer status;
}
