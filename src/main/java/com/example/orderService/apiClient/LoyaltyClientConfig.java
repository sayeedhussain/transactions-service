package com.example.orderService.apiClient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "api-client.loyalties")
@Getter
@Setter
public class LoyaltyClientConfig {
  private String url;
}
