package com.example.orderService.security;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.orderService.OrderService;
import com.example.orderService.model.OrderItemDTO;
import com.github.tomakehurst.wiremock.WireMockServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class OAuthIntegrationTest {

  @LocalServerPort
  private int port;

  private static WireMockServer wireMockServer;

  @Autowired
  private RestTemplate restTemplate;

  @MockBean
  OrderService orderService;

  private static String jwks;

  @BeforeAll
  public static void setUp() throws Exception {
    wireMockServer = new WireMockServer(8083);
    wireMockServer.start();
    configureFor("localhost", 8083);

    jwks = JwtUtil.generateJwksFromPublicKey();
  }

  @AfterAll
  public static void tearDown() {
    wireMockServer.stop();
  }

  @Test
  public void shouldReturn401WhenNoTokenIsProvided() {

    // Given
    stubForJWKS(jwks);

    String url = url("/api/orders");
    HttpEntity<Map<String, Object>> requestEntity = createOrderRequestEntity(new HttpHeaders());

    // When
    try {
      restTemplate.postForObject(
          url,
          requestEntity,
          String.class);
      fail("Expected exception to be thrown");
    } catch (HttpClientErrorException e) {
      // Then
      assertEquals(HttpStatusCode.valueOf(401), e.getStatusCode());
    }
  }

  @Test
  public void shouldReturn403WhenScopeIsInvalid() throws Exception {

    // Given
    stubForJWKS(jwks);

    String invalidToken = JwtUtil.generateJwt("http://localhost:8083", Map.of("scope", "dummy"));

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(invalidToken);
    String url = url("/api/orders");
    HttpEntity<Map<String, Object>> requestEntity = createOrderRequestEntity(headers);

    // When
    try {
      restTemplate.postForObject(
          url,
          requestEntity,
          String.class);
      fail("Expected exception to be thrown"); // fail test if exception is not thrown after 3 retries
    } catch (HttpClientErrorException e) {
      // Then
      assertEquals(HttpStatusCode.valueOf(403), e.getStatusCode());
      wireMockServer.verify(1, getRequestedFor(urlEqualTo("/.well-known/openid-configuration")));
      wireMockServer.verify(1, getRequestedFor(urlEqualTo("/.well-known/jwks.json")));
    }
  }

  @Test
  public void shouldReturn200WhenAccessTokenIsValid() throws Exception {

    // Given
    stubForJWKS(jwks);

    String validToken = JwtUtil.generateJwt("http://localhost:8083", Map.of("scope", "order:write"));

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(validToken);
    String url = url("/api/orders");
    HttpEntity<Map<String, Object>> requestEntity = createOrderRequestEntity(headers);

    // When
    ResponseEntity<String> response = restTemplate.postForEntity(
        url,
        requestEntity,
        String.class);

    // Then
    assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
    wireMockServer.verify(1, getRequestedFor(urlEqualTo("/.well-known/openid-configuration")));
    wireMockServer.verify(1, getRequestedFor(urlEqualTo("/.well-known/jwks.json")));
  }

  private String url(String endpoint) {
    return "http://localhost:" + port + endpoint;
  }

  private void stubForJWKS(String jwks) {
    stubFor(get("/.well-known/openid-configuration")
      .willReturn(okJson("{\"issuer\": \"http://localhost:8083\",\"jwks_uri\": \"http://localhost:8083/.well-known/jwks.json\"}")));
    stubFor(get("/.well-known/jwks.json")
      .willReturn(okJson(jwks)));
  }

  private HttpEntity<Map<String, Object>> createOrderRequestEntity(HttpHeaders headersToAdd) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.addAll(headersToAdd);

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("customerId", 1L);
    requestBody.put("items", List.of(new OrderItemDTO("macbook pro", 2, BigDecimal.valueOf(1000))));

    return new HttpEntity<>(requestBody, headers);
  }
}
