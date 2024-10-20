package com.example.integrationtests.client;

import com.example.integrationtests.model.NotificationRequest;
import com.example.integrationtests.model.NotificationResponse;
import com.example.integrationtests.model.Order;
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
}
