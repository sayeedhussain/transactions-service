package com.example.orderService.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderDTO {
  private Long customerId;
  private List<OrderItemDTO> items;

  public BigDecimal getAmount() {
    return items.stream()
      .map(OrderItemDTO::getAmount)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
