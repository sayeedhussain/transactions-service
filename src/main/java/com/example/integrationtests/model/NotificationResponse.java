package com.example.integrationtests.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NotificationResponse {
    private Long id;
    private Long customerId;
    private String orderNumber;
    private String message;
    private LocalDateTime createdAt;
}
