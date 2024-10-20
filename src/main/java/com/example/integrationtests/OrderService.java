package com.example.integrationtests;

import com.example.integrationtests.client.NotificationClient;
import com.example.integrationtests.db.OrderRepository;
import com.example.integrationtests.model.NotificationResponse;
import com.example.integrationtests.model.Order;
import com.example.integrationtests.model.OrderDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final NotificationClient notificationClient;

    public OrderService(OrderRepository orderRepository, NotificationClient notificationClient) {
        this.orderRepository = orderRepository;
        this.notificationClient = notificationClient;
    }

    public void placeOrder(OrderDTO orderDTO) {
        Order order = new Order(
                orderDTO.getCustomerId(),
                "ORD_AAX123",
                "PENDING",
                orderDTO.getAmount(),
                LocalDateTime.now()
                );
        System.out.print(order.getId());
        orderRepository.save(order);
        System.out.print(order.getId());
        NotificationResponse response = notificationClient.sendOrderNotification(order);
        System.out.println("Notification ID: " + response.getId());
    }
}
