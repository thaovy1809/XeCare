package com.group3.xecare2.admin.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private BigDecimal price;
    private Integer durationDays;
    private String features;
    private Boolean isActive = true;
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "subscription")
    private List<GarageSubscription> garageSubscriptions;
}