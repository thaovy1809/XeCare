package com.group3.xecare2.admin.entities;

import java.time.LocalDateTime;

import com.group3.xecare2.garage.entities.Garage;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Garage_Subscriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarageSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Subscription subscription;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isActive = true;
    
    @PrePersist
    protected void onCreate() {
        if (this.isActive == null) {
            this.isActive = true;
        }
    }
}