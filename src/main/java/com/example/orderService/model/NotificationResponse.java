package com.example.orderService.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotificationResponse {
  private Long id;
  private Long customerId;
  private String orderNumber;
  private String message;
  private LocalDateTime createdAt;
}
