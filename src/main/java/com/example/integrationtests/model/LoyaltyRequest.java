package com.example.integrationtests.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoyaltyRequest {
    private Long customerId;
    private String orderNumber;
    private BigDecimal orderAmount;
}
