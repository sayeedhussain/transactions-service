package com.example.integrationtests.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
