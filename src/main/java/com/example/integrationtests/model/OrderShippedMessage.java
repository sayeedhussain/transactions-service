package com.example.integrationtests.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderShippedMessage {
  private Long orderId;
  private String orderNumber;
  private String trackingNumber;
  private LocalDateTime shippedDate;

  @Override
  public String toString() {
    return "OrderShippedMessage{" +
        "orderId='" + orderId + '\'' +
        "orderNumber='" + orderNumber + '\'' +
        ", trackingNumber='" + trackingNumber + '\'' +
        ", shippedDate=" + shippedDate +
        '}';
  }
}
