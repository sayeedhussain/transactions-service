package com.example.orderService.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class OrderPlacedMessage {

  private final Long orderId;
  private final Long customerId;
  private final String orderNumber;
  private final BigDecimal totalAmount;
  private final LocalDateTime orderDate;

  public OrderPlacedMessage(Order order) {
    this.orderId = order.getId();
    this.customerId = order.getCustomerId();
    this.orderNumber = order.getOrderNumber();
    this.totalAmount = order.getTotalAmount();
    this.orderDate = order.getOrderDate();
  }
}
