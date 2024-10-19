package com.thoughtworks.test_pyramid;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Order {

    @Id
    @GeneratedValue
    private Long id;
    private String orderId;

    // Getters and Setters
}
