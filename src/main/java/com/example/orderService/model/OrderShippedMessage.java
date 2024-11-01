package com.example.orderService.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderShippedMessage {
  private Long orderId;
  private String orderNumber;
  private String trackingNumber;
  private LocalDateTime shippedDate;
}
