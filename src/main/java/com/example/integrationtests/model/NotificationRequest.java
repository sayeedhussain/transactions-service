package com.example.integrationtests.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NotificationRequest {
  private Long customerId;
  private String orderNumber;
  private String message;
}
