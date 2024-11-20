package com.example.orderService.apiClient;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.example.orderService.model.NotificationRequest;
import com.example.orderService.model.NotificationResponse;
import com.example.orderService.model.Order;

@Service
public class NotificationClient {

  private final RestTemplate restTemplate;
  private final NotificationClientConfig notificationClientConfig;

  public NotificationClient(RestTemplate restTemplate, NotificationClientConfig notificationClientConfig) {
    this.restTemplate = restTemplate;
    this.notificationClientConfig = notificationClientConfig;
  }

  /*
   * INFO: 1. Add happy path integration test for one api call (200 OK) in each ApiClient. Unit test the remaining logic. 2. Add integration test for @Retryable
   */
  @Retryable(
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2))
  public NotificationResponse sendOrderNotification(Order order) {
    NotificationRequest request = new NotificationRequest(
        order.getCustomerId(), order.getOrderNumber(), "Order placed successfully");

    try {
      return restTemplate.postForObject(
          notificationClientConfig.getUrl(), request, NotificationResponse.class);

    } catch (HttpStatusCodeException e) {
      System.err.println("Failed to send notification. Status: " + e.getStatusCode());
      throw e;
    } catch (Exception e) {
      System.err.println("An error occurred: " + e.getMessage());
      throw e;
    }
  }

  @Recover
  public NotificationResponse recover(Exception e, NotificationRequest request) throws Exception {
    // Handle the case when all retry attempts fail
    System.err.println("Failed to send notification after retries: " + e.getMessage());
    throw e;
  }

  /*
   * INFO: 1. Given one api call (above) is integration tested, this and any other api calls in this class can be unit tested by mocking RestTemplate. 2. No need to add integration test for @Retryable
   * as it is already integration tested for above api call
   */
  @Retryable(
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2))
  public NotificationResponse getNotification(String notificationId) {
    String url = notificationClientConfig.getUrl() + notificationId;
    try {
      return restTemplate.getForObject(
          url, NotificationResponse.class);

    } catch (HttpStatusCodeException e) {
      System.err.println("Failed to send notification. Status: " + e.getStatusCode());
      throw e;
    } catch (Exception e) {
      System.err.println("An error occurred: " + e.getMessage());
      throw e;
    }
  }
}
