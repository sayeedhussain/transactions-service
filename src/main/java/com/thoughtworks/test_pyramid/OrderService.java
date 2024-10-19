package com.thoughtworks.test_pyramid;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final RabbitTemplate rabbitTemplate;

    public OrderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void placeOrder(String orderId) {
        // Logic to place order in database (not shown)
        
        // Send a message to RabbitMQ
        rabbitTemplate.convertAndSend("order.queue", orderId);
    }
}
