package com.example.orderService.apiClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.orderService.model.NotificationResponse;
import com.example.orderService.model.Order;
import com.example.orderService.model.OrderStatus;
import com.github.tomakehurst.wiremock.WireMockServer;


@SpringBootTest
@ActiveProfiles("test")
public class NotificationClientTest {

  @Autowired
  NotificationClient notificationClient;

  private static WireMockServer wireMockServer;

  @BeforeAll
  static void setup() {
    wireMockServer = new WireMockServer(8082);
    wireMockServer.start();
    configureFor("localhost", 8082);
  }

  @AfterAll
  static void teardown() {
    wireMockServer.stop();
  }

  @Test
  public void testSendNotification() {
    // Given
    Order order = new Order(
        123L,
        "ORD_AAX122",
        OrderStatus.PENDING,
        new BigDecimal("499.99"),
        LocalDateTime.now());
    stubForPostNotificationSuccess();

    // When
    NotificationResponse response = notificationClient.sendOrderNotification(order);

    // Then
    assertEquals(101L, response.getId());
    assertEquals(123L, response.getCustomerId());
    assertEquals("ORD_AAX122", response.getOrderNumber());
    assertEquals(LocalDateTime.parse("2024-10-19T10:45:30"), response.getCreatedAt());
    assertEquals("Order placed successfully", response.getMessage());

    verify(postRequestedFor(urlEqualTo("/notifications")));
  }

  @Test
  public void testSendNotificationRetryOnFailure() {
    // Given
    Order order = new Order(
        133L,
        "ORD_AAX122",
        OrderStatus.PENDING,
        new BigDecimal("499.99"),
        LocalDateTime.now());
    stubForPostNotificationFailure();

    // When
    try {
      notificationClient.sendOrderNotification(order);
      fail("Expected exception to be thrown"); // fail test if exception is not thrown after 3 retries
    } catch (Exception e) {
      // Then
      verify(3, postRequestedFor(urlEqualTo("/notifications")).withRequestBody(
          equalToJson("{\"customerId\":133,\"orderNumber\":\"ORD_AAX122\",\"message\":\"Order placed successfully\"}")));
    }
  }

  private void stubForPostNotificationSuccess() {
    stubFor(post(urlEqualTo("/notifications"))
      .withRequestBody(equalToJson("{\"customerId\":123,\"orderNumber\":\"ORD_AAX122\",\"message\":\"Order placed successfully\"}"))
      .willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody(
            "{\"id\":101,\"customerId\":123,\"orderNumber\":\"ORD_AAX122\",\"message\":\"Order placed successfully\",\"createdAt\":\"2024-10-19T10:45:30\"}")));
  }

  private void stubForPostNotificationFailure() {
    stubFor(post(urlEqualTo("/notifications"))
      .withRequestBody(equalToJson("{\"customerId\":133,\"orderNumber\":\"ORD_AAX122\",\"message\":\"Order placed successfully\"}"))
      .willReturn(aResponse()
        .withStatus(500)
        .withHeader("Content-Type", "application/json")
        .withBody("Server Error")));
  }
}
