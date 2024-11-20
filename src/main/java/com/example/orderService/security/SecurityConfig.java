package com.example.orderService.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/*
 * INFO: Add 3 integration tests for 200, 401, 403 for any one authenticated endpoint of the application. If oauth is wired up for any one endpoint properly and calls are happening to the auth
 * provider, it will work for all endpoints of the application. Remaining auth configuration/logic can be covered in unit tested of this (SecurityConfig) class. Refer OAuthIntegrationTest class. We
 * have added 3 tests for POST /orders endpoint. No more oauth integration tests are required for the application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${enableAuth:true}") // default true
  private Boolean enableAuth;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    return enableAuth ? enableAuth(httpSecurity) : disableAuth(httpSecurity);
  }

  private SecurityFilterChain disableAuth(HttpSecurity httpSecurity) throws Exception {
    System.out.println("enableAuth: " + false);
    return httpSecurity
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
      .build();
  }

  private SecurityFilterChain enableAuth(HttpSecurity httpSecurity) throws Exception {
    System.out.println("enableAuth: " + true);
    return httpSecurity
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(this::authorizeRequests)
      .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
      .build();
  }

  private void authorizeRequests(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
    auth
      .requestMatchers(request -> request.getRequestURI().equals("/api/orders"))
      .hasAuthority("SCOPE_order:write");
  }

}
