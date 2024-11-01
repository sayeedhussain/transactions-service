package com.example.orderService.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoyaltyRequest {
  private Long customerId;
  private String orderNumber;
  private BigDecimal orderAmount;
}
