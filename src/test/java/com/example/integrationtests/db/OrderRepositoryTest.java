package com.example.integrationtests.db;

import com.example.integrationtests.Order;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Test
    void shouldSaveOrder() {
        Order order = new Order(
                "ORD12345",
                LocalDateTime.now(),
                "PENDING",
                new BigDecimal("499.99"),
                101L
        );
        orderRepository.save(order);

        Optional<Order> savedOrder = orderRepository.findById(1L);

        assertEquals(101L, savedOrder.get().getCustomerId());
    }

}
