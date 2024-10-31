package com.example.integrationtests.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class LoyaltyRequest {
  private Long customerId;
  private String orderNumber;
  private BigDecimal orderAmount;
}
