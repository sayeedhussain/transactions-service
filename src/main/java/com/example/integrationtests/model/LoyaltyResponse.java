package com.example.integrationtests.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class LoyaltyResponse {
  private Long id;
  private Long customerId;
  private String orderNumber;
  private BigDecimal orderAmount;
  private BigDecimal loyaltyPointsAdded;
  private LocalDateTime createdAt;
}
