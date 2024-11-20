package com.example.orderService.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.orderService.model.Order;
import com.example.orderService.model.OrderStatus;

// We use @DataJpaTest here instead of @SpringbootTest to increase speed by loading minimal Spring context.
// Refer this for more details https://www.baeldung.com/junit-datajpatest-repository
@DataJpaTest
@ActiveProfiles("test")
public class OrderRepositoryTest {

  @Autowired
  OrderRepository orderRepository;

  @Test
  void shouldSaveOrder() {
    // Given
    Order order = new Order(
        101L,
        "ORD_AAX111",
        OrderStatus.PENDING,
        new BigDecimal("499.99"),
        LocalDateTime.now());
    orderRepository.save(order);

    // When
    Optional<Order> savedOrder = orderRepository.findById(order.getId());

    // Then
    assertEquals("ORD_AAX111", savedOrder.get().getOrderNumber());
  }

}
