package com.example.integrationtests.security;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

  @BeforeAll
  public static void setUp() throws IOException {
    wireMockServer = new WireMockServer(8083);
    wireMockServer.start();
    configureFor("localhost", 8083);

    System.setProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri", "http://localhost:8083");
  }

  @AfterAll
  public static void tearDown() throws IOException {
    wireMockServer.stop();
  }

  @Test
  public void shouldReturn401WhenNoTokenProvided() {

    stubFor(get("/.well-known/openid-configuration")
      .willReturn(okJson("{\"issuer\": \"http://localhost:8083\",\"jwks_uri\": \"http://localhost:8083/.well-known/jwks.json\"}")));

    stubFor(get("/.well-known/jwks.json")
            .willReturn(okJson(
                    "{\"keys\":[{\"alg\": \"RS256\",\"e\": \"AQAB\",\"key_ops\": [\"verify\"],\"kty\": \"RSA\",\"n\": \"tI-tVUWI1KkhUVQsmpTszOSChHGKgZ49er-UTZzpZyOcrCd3TOgdeJNEON2D92OiF-IxXxBC9PW77x4M8hHEr5OJqKtNIS0XdKVbUD6TtcOeOgqK6sakWrg08qyoeKG04fAn7MK8snmkuhyEkYkwiHPI3KhA3z6M2vdliC0Gz-HzyK3BlxpvNBXYWlWWYi0Its7gfNfSxOb7cAv4osqjBT1wJDGqAAGMLIcnpKIwugJ12KVObYPPKERdLIuvfLeA6cRt6GPRG0KTHbX6NIC2Sosm2nrrDO2RRVRkDeZQ-ZS5rwjXxNGtRSnRU_I4EyeUT95QV6FdGfj9NKisM44Qn6ndeOuy3_btmHUY2yuYHNRmgknbbEM48MnVWR1QP9lN9JxIufdUcJ81KvW2fbhEpwAm6srjmyCyUpo-1kNbnDmS_Irg4uQJyZ5p7WghHXPayJW_CG0NfN0Gcsu2hRDTpjRNijzB5glHxNjhLPq31zA7IJTyFA5X0kFocmQEOvYB\",\"use\": \"sig\",\"kid\": \"52eba95d00e4c11d3ccbaf6b913ae45a\"}]}")));

    String url = url("/api/orders");
    HttpEntity<Map<String, Object>> requestEntity = createOrderRequestEntity(new HttpHeaders());

    // When
    try {
      restTemplate.exchange(
          url,
          HttpMethod.POST,
          requestEntity,
          String.class);
      fail("Expected exception to be thrown"); // fail test if exception is not thrown after 3 retries
    } catch (HttpClientErrorException e) {
      // Then
      assertEquals(HttpStatusCode.valueOf(401), e.getStatusCode());
    }
  }

  @Test
  public void shouldReturn403WhenScopeIsInvalid() {

    stubFor(get("/.well-known/openid-configuration")
      .willReturn(okJson("{\"issuer\": \"http://localhost:8083\",\"jwks_uri\": \"http://localhost:8083/.well-known/jwks.json\"}")));

    stubFor(get("/.well-known/jwks.json")
      .willReturn(okJson(
          "{\"keys\":[{\"alg\": \"RS256\",\"e\": \"AQAB\",\"key_ops\": [\"verify\"],\"kty\": \"RSA\",\"n\": \"tI-tVUWI1KkhUVQsmpTszOSChHGKgZ49er-UTZzpZyOcrCd3TOgdeJNEON2D92OiF-IxXxBC9PW77x4M8hHEr5OJqKtNIS0XdKVbUD6TtcOeOgqK6sakWrg08qyoeKG04fAn7MK8snmkuhyEkYkwiHPI3KhA3z6M2vdliC0Gz-HzyK3BlxpvNBXYWlWWYi0Its7gfNfSxOb7cAv4osqjBT1wJDGqAAGMLIcnpKIwugJ12KVObYPPKERdLIuvfLeA6cRt6GPRG0KTHbX6NIC2Sosm2nrrDO2RRVRkDeZQ-ZS5rwjXxNGtRSnRU_I4EyeUT95QV6FdGfj9NKisM44Qn6ndeOuy3_btmHUY2yuYHNRmgknbbEM48MnVWR1QP9lN9JxIufdUcJ81KvW2fbhEpwAm6srjmyCyUpo-1kNbnDmS_Irg4uQJyZ5p7WghHXPayJW_CG0NfN0Gcsu2hRDTpjRNijzB5glHxNjhLPq31zA7IJTyFA5X0kFocmQEOvYB\",\"use\": \"sig\",\"kid\": \"52eba95d00e4c11d3ccbaf6b913ae45a\"}]}")));

    String invalidToken =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IjUyZWJhOTVkMDBlNGMxMWQzY2NiYWY2YjkxM2FlNDVhIn0.eyJzdWIiOiJ1c2VyMTIzIiwicHJpbmNpcGFsIjoidXNlcjEyMyIsInNjb3BlIjoiU0NPUEVfb3JkZXI6cmVhZCIsImlhdCI6MTczMDA1MjMyNTg5OSwiZXhwIjoxNzMwMDczMzI1ODk5LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODMifQ.nLbwSjpRR9paJKJNDYx7EklS-Svi1MPMa1djtBdlGpBjRzqQrn6Riob-K9RLDM5Ov-VJZ0ot5av1aJpNWoAdVyBbIiRqFMqcmy6HCg6WkVYlZBFCWZOR6BDlb5IOPd3bb7hNDnJPrE-h4_hiUJKpVjny97FYASBNHu202cFo_zALYiQN_VHvF27XFp945JVtaMw42f2sUNyuAtfvCqi8yrfngN-in7MtWlZn11R5ncphjA6YzvbMWSGUVIj2_bLvXU4-wL8tPUAJViyxgPXWWazagADtvGx-4pVTi-7Muwh4ddNX0tvG-HnQly-XJiXTAE_FlqwuHBUJuG05tnCQwO_2r0TAQTD8sMhTodxCAjyOPGvsQPBkxQaJ_A73jHXVZFN2KATc3igVVAZQppKcX_MfEDQD3HDuStp_2cq0TJm-c_6GOWL9gsDL6BO3iJTDE78Kpve7jyl_gvs9ymP1CdfSCxKFSYpN1hdQQ9iKS4y9OJu1HTxBCsTtHGFTm6rB";
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(invalidToken);
    String url = url("/api/orders");
    HttpEntity<Map<String, Object>> requestEntity = createOrderRequestEntity(headers);

    // When
    try {
      restTemplate.exchange(
          url,
          HttpMethod.POST,
          requestEntity,
          String.class);
      fail("Expected exception to be thrown"); // fail test if exception is not thrown after 3 retries
    } catch (HttpClientErrorException e) {
      // Then
      assertEquals(HttpStatusCode.valueOf(403), e.getStatusCode());
    }
  }

  @Test
  public void shouldReturn200WhenAccessTokenIsInvalid() {

    stubFor(get("/.well-known/openid-configuration")
      .willReturn(okJson("{\"issuer\": \"http://localhost:8083\",\"jwks_uri\": \"http://localhost:8083/.well-known/jwks.json\"}")));

    stubFor(get("/.well-known/jwks.json")
      .willReturn(okJson(
          "{\"keys\":[{\"alg\": \"RS256\",\"e\": \"AQAB\",\"key_ops\": [\"verify\"],\"kty\": \"RSA\",\"n\": \"tI-tVUWI1KkhUVQsmpTszOSChHGKgZ49er-UTZzpZyOcrCd3TOgdeJNEON2D92OiF-IxXxBC9PW77x4M8hHEr5OJqKtNIS0XdKVbUD6TtcOeOgqK6sakWrg08qyoeKG04fAn7MK8snmkuhyEkYkwiHPI3KhA3z6M2vdliC0Gz-HzyK3BlxpvNBXYWlWWYi0Its7gfNfSxOb7cAv4osqjBT1wJDGqAAGMLIcnpKIwugJ12KVObYPPKERdLIuvfLeA6cRt6GPRG0KTHbX6NIC2Sosm2nrrDO2RRVRkDeZQ-ZS5rwjXxNGtRSnRU_I4EyeUT95QV6FdGfj9NKisM44Qn6ndeOuy3_btmHUY2yuYHNRmgknbbEM48MnVWR1QP9lN9JxIufdUcJ81KvW2fbhEpwAm6srjmyCyUpo-1kNbnDmS_Irg4uQJyZ5p7WghHXPayJW_CG0NfN0Gcsu2hRDTpjRNijzB5glHxNjhLPq31zA7IJTyFA5X0kFocmQEOvYB\",\"use\": \"sig\",\"kid\": \"52eba95d00e4c11d3ccbaf6b913ae45a\"}]}")));

    String validToken =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IjUyZWJhOTVkMDBlNGMxMWQzY2NiYWY2YjkxM2FlNDVhIn0.eyJzdWIiOiJ1c2VyMTIzIiwicHJpbmNpcGFsIjoidXNlcjEyMyIsInNjb3BlIjoiU0NPUEVfb3JkZXI6cmVhZCIsImlhdCI6MTczMDA1MjMyNTg5OSwiZXhwIjoxNzMwMDczMzI1ODk5LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODMifQ.nLbwSjpRR9paJKJNDYx7EklS-Svi1MPMa1djtBdlGpBjRzqQrn6Riob-K9RLDM5Ov-VJZ0ot5av1aJpNWoAdVyBbIiRqFMqcmy6HCg6WkVYlZBFCWZOR6BDlb5IOPd3bb7hNDnJPrE-h4_hiUJKpVjny97FYASBNHu202cFo_zALYiQN_VHvF27XFp945JVtaMw42f2sUNyuAtfvCqi8yrfngN-in7MtWlZn11R5ncphjA6YzvbMWSGUVIj2_bLvXU4-wL8tPUAJViyxgPXWWazagADtvGx-4pVTi-7Muwh4ddNX0tvG-HnQly-XJiXTAE_FlqwuHBUJuG05tnCQwO_2r0TAQTD8sMhTodxCAjyOPGvsQPBkxQaJ_A73jHXVZFN2KATc3igVVAZQppKcX_MfEDQD3HDuStp_2cq0TJm-c_6GOWL9gsDL6BO3iJTDE78Kpve7jyl_gvs9ymP1CdfSCxKFSYpN1hdQQ9iKS4y9OJu1HTxBCsTtHGFTm6rB";
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(validToken);
    String url = url("/api/orders");
    HttpEntity<Map<String, Object>> requestEntity = createOrderRequestEntity(headers);

    // When
    try {
      restTemplate.exchange(
          url,
          HttpMethod.POST,
          requestEntity,
          String.class);
      fail("Expected exception to be thrown"); // fail test if exception is not thrown after 3 retries
    } catch (HttpClientErrorException e) {
      // Then
      assertEquals(HttpStatusCode.valueOf(200), e.getStatusCode());
    }
  }

  private String url(String endpoint) {
    return "http://localhost:" + port + endpoint;
  }

  private HttpEntity<Map<String, Object>> createOrderRequestEntity(HttpHeaders headersToAdd) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.addAll(headersToAdd);

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("name", "John Doe");
    requestBody.put("email", "john.doe@example.com");

    return new HttpEntity<>(requestBody, headers);
  }
}
