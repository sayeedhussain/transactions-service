package com.example.integrationtests.apiClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.integrationtests.model.LoyaltyResponse;
import com.example.integrationtests.model.Order;
import com.example.integrationtests.model.OrderStatus;
import com.github.tomakehurst.wiremock.WireMockServer;

@SpringBootTest
@ActiveProfiles("test")
public class LoyalClientTest {

  @Autowired
  LoyaltyClient loyaltyClient;

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
  public void testAddLoyalty() {
    // Given
    Order order = new Order(
        123L,
        "ORD_AAX124",
        OrderStatus.PENDING,
        new BigDecimal("499.99"),
        LocalDateTime.now());

    stubForPostLoyaltySuccess();

    // When
    LoyaltyResponse response = loyaltyClient.addLoyalty(order);

    // Then
    assertEquals(101L, response.getId());
    assertEquals(123L, response.getCustomerId());
    assertEquals("ORD_AAX124", response.getOrderNumber());
    assertEquals(BigDecimal.valueOf(499.99), response.getOrderAmount());
    assertEquals(BigDecimal.valueOf(50), response.getLoyaltyPointsAdded());
    assertEquals(LocalDateTime.parse("2024-10-19T10:45:30"), response.getCreatedAt());

    verify(postRequestedFor(urlEqualTo("/loyalties")));
  }

  private void stubForPostLoyaltySuccess() {
    stubFor(post(urlEqualTo("/loyalties"))
      .withRequestBody(equalToJson("{\"customerId\":123,\"orderNumber\":\"ORD_AAX124\",\"orderAmount\":499.99}"))
      .willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "application/json")
        .withBody(
            "{\"id\":101,\"customerId\":123,\"orderNumber\":\"ORD_AAX124\",\"orderAmount\":499.99,\"loyaltyPointsAdded\":50,\"createdAt\":\"2024-10-19T10:45:30\"}")));
  }

}
