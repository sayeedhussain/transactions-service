package com.example.orderService.apiClient;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.example.orderService.model.*;

@Service
public class LoyaltyClient {

  private final RestTemplate restTemplate;
  private final LoyaltyClientConfig loyaltyClientConfig;

  public LoyaltyClient(RestTemplate restTemplate, LoyaltyClientConfig loyaltyClientConfig) {
    this.restTemplate = restTemplate;
    this.loyaltyClientConfig = loyaltyClientConfig;
  }

  /* INFO:
   * 1. Add integration test for one api call (200 OK) in each ApiClient. Unit test the remaining logic.
   * 2. No need to add integration test for @Retryable as it is already integration tested in NotificationClient.
   */
  @Retryable(
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2))
  public LoyaltyResponse addLoyalty(Order order) {
    LoyaltyRequest request = new LoyaltyRequest(
        order.getCustomerId(),
        order.getOrderNumber(),
        order.getTotalAmount());

    try {
      return restTemplate.postForObject(
          loyaltyClientConfig.getUrl(), request, LoyaltyResponse.class);

    } catch (HttpStatusCodeException e) {
      System.err.println("Failed to send notification. Status: " + e.getStatusCode());
      throw e;
    } catch (Exception e) {
      System.err.println("An error occurred: " + e.getMessage());
      throw e;
    }
  }

}
