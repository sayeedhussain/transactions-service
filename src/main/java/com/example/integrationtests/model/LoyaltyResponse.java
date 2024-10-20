package com.example.integrationtests.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoyaltyResponse {
    private Long id;
    private Long customerId;
    private String orderNumber;
    private BigDecimal orderAmount;
    private BigDecimal loyaltyPointsAdded;
    private LocalDateTime createdAt;
}
