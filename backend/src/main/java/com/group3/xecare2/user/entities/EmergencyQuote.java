package com.group3.xecare2.user.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.group3.xecare2.enums.QuoteStatus;
import com.group3.xecare2.garage.entities.Garage;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "Emergency_Quotes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyQuote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private EmergencyRequest request;

    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;

    private BigDecimal estimatedCost;
    private Integer estimatedTime;

    @Enumerated(EnumType.STRING)
    private QuoteStatus status;

    private String notes;
    private LocalDateTime quotedAt = LocalDateTime.now();
    //Hoang
    @PrePersist
    protected void onCreate() {
        if (this.quotedAt == null) {
            this.quotedAt = LocalDateTime.now();
        }
    }
}
