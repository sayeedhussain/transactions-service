package com.example.orderService.apiClient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "api-client.notifications")
@Getter
@Setter
public class NotificationClientConfig {
  private String url;
}
