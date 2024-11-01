package com.example.integrationtests.security;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.*;

public class JwtUtil {

  public static String generateJwt(
      String issuer,
      Map<String, Object> claims) throws Exception {
    // Load private key
    PrivateKey privateKey = loadPrivateKey();

    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);

    return Jwts.builder()
      .setIssuer(issuer)
      .addClaims(claims)
      .setIssuedAt(now)
      .setExpiration(new Date(nowMillis + 60000)) // 1 min expiration
      .signWith(privateKey, SignatureAlgorithm.RS256)
      .compact();
  }

  private static PrivateKey loadPrivateKey() throws Exception {
    byte[] keyBytes;
    try (InputStream inputStream = JwtUtil.class.getClassLoader().getResourceAsStream("private.pem")) {
      if (inputStream == null) {
        throw new IllegalArgumentException("File not found: private.pem");
      }

      keyBytes = inputStream.readAllBytes();
    }
    String privateKeyPEM = new String(keyBytes)
      .replace("-----BEGIN PRIVATE KEY-----", "")
      .replace("-----END PRIVATE KEY-----", "")
      .replaceAll("\\s", "");

    byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePrivate(keySpec);
  }

  public static String generateJwksFromPublicKey() throws Exception {
    // Load public key
    PublicKey publicKey = loadPublicKey();

    // Convert the public key to an RSA JWK
    RSAKey rsaJwk = new RSAKey.Builder((RSAPublicKey) publicKey)
      .keyID(UUID.randomUUID().toString()) // Set a unique Key ID
      .keyUse(KeyUse.SIGNATURE) // Specify use as signature
      .algorithm(JWSAlgorithm.RS256) // Specify the algorithm
      .build();

    // Create a JWK Set with the RSA Key
    JWKSet jwkSet = new JWKSet(rsaJwk);

    // Output JWKS JSON
    return jwkSet.toJSONObject().toString();
  }

  private static PublicKey loadPublicKey() throws Exception {
    byte[] keyBytes;
    try (InputStream inputStream = JwtUtil.class.getClassLoader().getResourceAsStream("public.pem")) {
      if (inputStream == null) {
        throw new IllegalArgumentException("File not found: public.pem");
      }

      keyBytes = inputStream.readAllBytes();
    }

    String publicKeyPEM = new String(keyBytes)
      .replace("-----BEGIN PUBLIC KEY-----", "")
      .replace("-----END PUBLIC KEY-----", "")
      .replaceAll("\\s", "");

    byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePublic(keySpec);
  }

}
