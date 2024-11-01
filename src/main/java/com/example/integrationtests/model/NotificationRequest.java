package com.example.integrationtests.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotificationRequest {
  private Long customerId;
  private String orderNumber;
  private String message;
}
