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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${enableAuth}")
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
      .requestMatchers(request -> request.getMethod().equals("POST") &&
          request.getRequestURI().equals("/api/ordersz"))
      .hasAuthority("SCOPE_order:write");
  }

}
