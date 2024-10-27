package com.example.integrationtests.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderItemDTO {
  private String name;
  private Integer qty;
  private BigDecimal price;

  public BigDecimal getAmount() {
    return price.multiply(BigDecimal.valueOf(qty));
  }
}
