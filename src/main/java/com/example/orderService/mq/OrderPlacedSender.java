package com.example.orderService.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.orderService.model.Order;
import com.example.orderService.model.OrderPlacedMessage;


@Service
public class OrderPlacedSender {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  /* INFO:
   * 1. Add one happy path integration test for each rabbitmq message sender.
   */
  public void sendOrderPlacedMessage(Order order) {
    OrderPlacedMessage message = new OrderPlacedMessage(order);
    rabbitTemplate.convertAndSend("order.exchange", "order.placed", message);
    System.out.println("Order Placed Message Sent: " + message);
  }
}
