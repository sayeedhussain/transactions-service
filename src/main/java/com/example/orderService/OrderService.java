package com.example.orderService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.example.orderService.apiClient.LoyaltyClient;
import com.example.orderService.apiClient.NotificationClient;
import com.example.orderService.db.OrderRepository;
import com.example.orderService.model.Order;
import com.example.orderService.model.OrderDTO;
import com.example.orderService.model.OrderStatus;
import com.example.orderService.mq.OrderPlacedSender;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final NotificationClient notificationClient;
  private final LoyaltyClient loyaltyClient;

  private final OrderPlacedSender orderPlacedSender;

  public OrderService(
      OrderRepository orderRepository,
      NotificationClient notificationClient,
      LoyaltyClient loyaltyClient,
      OrderPlacedSender orderPlacedSender) {
    this.orderRepository = orderRepository;
    this.notificationClient = notificationClient;
    this.loyaltyClient = loyaltyClient;
    this.orderPlacedSender = orderPlacedSender;
  }

  public Order placeOrder(OrderDTO orderDTO) {
    String orderNumber = generateOrderNumber();
    Order order = new Order(
        orderDTO.getCustomerId(),
        orderNumber,
        OrderStatus.PENDING,
        orderDTO.getAmount(),
        LocalDateTime.now());
    orderRepository.save(order);
    notificationClient.sendOrderNotification(order);
    loyaltyClient.addLoyalty(order);
    orderPlacedSender.sendOrderPlacedMessage(order);
    return order;
  }

  public Optional<Order> getOrder(Long orderId) {
    return orderRepository.findById(orderId);
  }

  private String generateOrderNumber() {
    Random random = new Random();

    // Generate first 3 capital letters (A-Z)
    String letters = IntStream.range(0, 3)
      .mapToObj(i -> "" + (char) ('A' + random.nextInt(26)))
      .collect(Collectors.joining());

    // Generate last 3 digits (0-9)
    String digits = IntStream.range(0, 3)
      .mapToObj(i -> "" + random.nextInt(10))
      .collect(Collectors.joining());

    // orderNumbers like ORD_ABC123
    return "ORD_" + letters + digits;
  }
}
