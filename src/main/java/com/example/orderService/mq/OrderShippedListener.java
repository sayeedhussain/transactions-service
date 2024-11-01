package com.example.orderService.mq;

import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.orderService.db.OrderRepository;
import com.example.orderService.model.Order;
import com.example.orderService.model.OrderShippedMessage;
import com.example.orderService.model.OrderStatus;

@Component
public class OrderShippedListener {

  private final OrderRepository orderRepository;

  public OrderShippedListener(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  /* INFO:
   * 1. Add one happy path integration test for each rabbitMQ message listener.
   */
  @RabbitListener(queues = "order.shipped.queue")
  public void handleOrderShippedMessage(OrderShippedMessage message) {
    System.out.println("Received Order Shipped Message: " + message);

    Optional<Order> optionalOrder = orderRepository.findById(message.getOrderId());
    if (optionalOrder.isEmpty()) {
      System.out.println("Order Not found {orderId}: " + message.getOrderId());
      return;
    }
    Order order = optionalOrder.get();
    order.setStatus(OrderStatus.SHIPPED);
    order.setTrackingNumber(message.getTrackingNumber());

    orderRepository.save(order);
  }
}
