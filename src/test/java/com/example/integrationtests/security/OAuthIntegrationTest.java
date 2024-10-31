package com.example.integrationtests.security;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.integrationtests.OrderService;
import com.example.integrationtests.model.OrderItemDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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

  @BeforeAll
  public static void setUp() {
    wireMockServer = new WireMockServer(8083);
    wireMockServer.start();
    configureFor("localhost", 8083);
  }

  @AfterAll
  public static void tearDown() {
    wireMockServer.stop();
  }

  @Test
  public void shouldReturn401WhenNoTokenIsProvided() {

    // Given
    stubForJWKS();

    String url = url("/api/orders");
    HttpEntity<Map<String, Object>> requestEntity = createOrderRequestEntity(new HttpHeaders());

    // When
    try {
      restTemplate.postForObject(
          url,
          requestEntity,
          String.class
      );
      fail("Expected exception to be thrown");
    } catch (HttpClientErrorException e) {
      // Then
      assertEquals(HttpStatusCode.valueOf(401), e.getStatusCode());
    }
  }

  @Test
  public void shouldReturn403WhenScopeIsInvalid() {

    // Given
    stubForJWKS();

    String invalidToken =
        "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgzIiwiaWF0IjoxNzMwMzkzNDEzLCJleHAiOjE3MzA0Mjk0MTN9.GuDXssMfcbzsD45GFsP6DeXOj2PCbVRDl2mv5pNXI18MPvQmnd5BEMdebwnI2RpLfgfjQxOzFpdW77XHaQsf34d1FNWNVS7rcnsp3SQMqNzFhLuSc9nNa26ne2CMoqt7QH3By3MIH4X_1Clnxmsrr4K1aCNuHaBSmnq9en559BNKoHYaajI_27sWYaMs_IbzoY_ynD7EY2QaKRWHhE6Ipquphsdu5AjQhnZt6QmPqYCq5VuLFWpkw-OP5HFZOfmdReRjDzetA3J-gReHzmSTQcMX3IzjT3v2RqJ20MfYZ5-ep2GliMn3X1nml_YL1Y87kZkEgbPrOAEhRH62HdlR1w";
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(invalidToken);
    String url = url("/api/orders");
    HttpEntity<Map<String, Object>> requestEntity = createOrderRequestEntity(headers);

    // When
    try {
      restTemplate.postForObject(
          url,
          requestEntity,
          String.class
      );
      fail("Expected exception to be thrown"); // fail test if exception is not thrown after 3 retries
    } catch (HttpClientErrorException e) {
      // Then
      assertEquals(HttpStatusCode.valueOf(403), e.getStatusCode());
    }
  }

  @Test
  public void shouldReturn200WhenAccessTokenIsInvalid() {

    // Given
    stubForJWKS();

    String validToken =
        "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgzIiwic2NvcGUiOiJvcmRlcjp3cml0ZSIsImlhdCI6MTczMDM5MzIxMywiZXhwIjoxNzMwNDI5MjEzfQ.B6bXWSbEF0C-CynitprR9P6NXkX23rD2683V6kanIvhkdqPEY57NzmhLf0TbrnDhOWVFz045HsJb56oYV2Cf1WQH6VXtV6ZGStA1jfQd30RmCPqrxFWcbC1cjbIZbLxCUvfVK-ND1vnNWg_rwLm0vACPEFQmAyGN8ItLVSTaq10iIHQvPUb51hosPQq7FqLjllxihQhQ7hT9V2sQy-sEgbWg_QeYC9-9-azw8hdzkQ1ZnGq9Xnml04K6ow_qd4h75aOKlyThtPoXDuFN7YorrhFHQMJhv3pWHir1h4sioKr71KnYC6THZUaiIWacmn3dqZNcJHPgKE5PsSBWOCJ4nQ";
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(validToken);
    String url = url("/api/orders");
    HttpEntity<Map<String, Object>> requestEntity = createOrderRequestEntity(headers);

    // When
     ResponseEntity<String> response = restTemplate.postForEntity(
          url,
          requestEntity,
          String.class
     );

     // Then
    assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
  }

  private String url(String endpoint) {
    return "http://localhost:" + port + endpoint;
  }

  private void stubForJWKS() {
    stubFor(get("/.well-known/openid-configuration")
            .willReturn(okJson("{\"issuer\": \"http://localhost:8083\",\"jwks_uri\": \"http://localhost:8083/.well-known/jwks.json\"}")));

    stubFor(get("/.well-known/jwks.json")
            .willReturn(okJson(
                    "{\"keys\":[{\"e\":\"AQAB\",\"kid\":\"GIL-lQB9RX4QAlWPYz-H82cCi9RybkSo0y_BLcFY5pk\",\"kty\":\"RSA\",\"n\":\"oSZ9MHrDvW2hKJei5m4CarcQ2R3Y5wX696cq8BFPdkGfsgNpSMeD-PsOTVQTtcp143B3BGlwp7Gti21Ku7cf2rL3XhNUyCthcGow2YJ89aVffy1hfXSuhKuivcTGZTPAcbGAbW2eoOmR46MPNnLue3dZIloVtnuPGD6R2N8EKQ8hT5evkgR6T7jVmeM2Pp-TgXygg13EQdIDkzrS26-Wjq5qfAWsqfxS1_T77fC4Q1XXp4VPsM-SGUzYHY6idSfprMHK3ndOVIl8DA3_hrVfmFQ3X15Su11Tsu6jNKCm5Q74Y5laoN5gBE-_98n94DJkN9NCqAYv8QlXS_ACV_T13w\"}]}")));
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
