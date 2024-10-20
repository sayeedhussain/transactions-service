package com.example.integrationtests.apiClient;

import com.example.integrationtests.model.NotificationRequest;
import com.example.integrationtests.model.NotificationResponse;
import com.example.integrationtests.model.Order;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationClient {

    private final RestTemplate restTemplate;
    private final NotificationClientConfig notificationClientConfig;

    public NotificationClient(RestTemplate restTemplate, NotificationClientConfig notificationClientConfig) {
        this.restTemplate = restTemplate;
        this.notificationClientConfig = notificationClientConfig;
    }

    /*
    Add integration test for one api call (200 OK) in each ApiClient. Unit test the remaining logic in each ApiClient.
     */
    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public NotificationResponse sendOrderNotification(Order order) {
        NotificationRequest request = new NotificationRequest(
                order.getCustomerId(), order.getOrderNumber(), "Order placed successfully"
        );

        try {
            NotificationResponse response = restTemplate.postForObject(
                    notificationClientConfig.getUrl(), request, NotificationResponse.class
            );
            return response;

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
        // For example, log the error and return a default response or throw a custom exception
        System.err.println("Failed to send notification after retries: " + e.getMessage());
        throw e;
    }

    /*
    Given one api call (above) is integration tested, this and any other api calls in this class can be unit tested by mocking RestTemplate.
     */
    public NotificationResponse getNotification(String notificationId) {
        String url = notificationClientConfig.getUrl() + notificationId;
        try {
            NotificationResponse response = restTemplate.getForObject(
                    url, NotificationResponse.class
            );
            return response;

        } catch (HttpStatusCodeException e) {
            System.err.println("Failed to send notification. Status: " + e.getStatusCode());
            throw e;
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            throw e;
        }
    }
}
