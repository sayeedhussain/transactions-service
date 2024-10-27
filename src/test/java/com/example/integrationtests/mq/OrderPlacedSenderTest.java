package com.example.integrationtests.mq;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.example.integrationtests.model.Order;
import com.example.integrationtests.model.OrderStatus;

@SpringBootTest
@ActiveProfiles("test")
public class OrderPlacedSenderTest {

  @Autowired
  OrderPlacedSender sender;

  @Autowired
  TestOrderPlacedListener listener;

  @Test
  public void testOrderPlacedMessageSend() throws InterruptedException {

    // Given
    Order order = new Order(
        101L,
        "ORD_AAX111",
        OrderStatus.PENDING,
        new BigDecimal("499.99"),
        LocalDateTime.now());

    // When
    sender.sendOrderPlacedMessage(order);
    Thread.sleep(2000); // wait for order placed message to be sent

    // Then
    boolean messageReceived = listener.getLatch().await(2, TimeUnit.SECONDS);
    TestOrderPlacedMessage message = listener.getReceivedMessage();
    assertEquals(Boolean.TRUE, messageReceived);
    assertEquals(101L, message.getCustomerId());
    assertEquals("ORD_AAX111", message.getOrderNumber());
  }
}


@Service
class TestOrderPlacedListener {

  private CountDownLatch latch = new CountDownLatch(1);
  private TestOrderPlacedMessage receivedMessage;

  @RabbitListener(queues = "order.placed.queue")
  public void listen(TestOrderPlacedMessage message) {
    this.receivedMessage = message;
    latch.countDown(); // Signal that the message was received
  }

  public TestOrderPlacedMessage getReceivedMessage() {
    return receivedMessage;
  }

  public CountDownLatch getLatch() {
    return latch;
  }
}


@Getter
@AllArgsConstructor
class TestOrderPlacedMessage {
  private Long orderId;
  private Long customerId;
  private String orderNumber;
  private BigDecimal totalAmount;
  private LocalDateTime orderDate;
}
