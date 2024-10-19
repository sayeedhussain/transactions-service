package com.thoughtworks.test_pyramid;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

    @RabbitListener(queues = "order.queue")
    public void receiveMessage(String orderId) {
        // Logic to process the order (not shown)
        System.out.println("Order processed: " + orderId);
    }
}
