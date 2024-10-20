package com.example.integrationtests.apiClient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api-client.loyalties")
@Getter
@Setter
public class LoyaltyClientConfig {
    private String url;
}
