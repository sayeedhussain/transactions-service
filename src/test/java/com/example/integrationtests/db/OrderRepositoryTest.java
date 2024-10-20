package com.example.integrationtests.db;

import com.example.integrationtests.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Test
    void shouldSaveOrder() {
        //Given
        Order order = new Order(
                101L,
                "ORD_AAX111",
                "PENDING",
                new BigDecimal("499.99"),
                LocalDateTime.now()
                );
        orderRepository.save(order);

        //When
        Optional<Order> savedOrder = orderRepository.findById(1L);

        //Then
        assertEquals(101L, savedOrder.get().getCustomerId());
    }

}
