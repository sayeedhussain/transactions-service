package com.thoughtworks.test_pyramid;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class OrderServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private OrderService orderService;

    public OrderServiceTest() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(rabbitTemplate);
    }

    @Test
    public void testPlaceOrder() {
        String orderId = "123";
        orderService.placeOrder(orderId);
        
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(rabbitTemplate).convertAndSend("order.queue", messageCaptor.capture());

        assertEquals(orderId, messageCaptor.getValue());
    }
}
