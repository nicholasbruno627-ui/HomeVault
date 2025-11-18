//need to test
package com.homevault.server.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class JWTService {

  private final Algorithm algorithm;
  private final long expirationMinutes;

  public JWTService(
      @Value("${homevault.jwt.secret}") String secret,
      @Value("${homevault.jwt.exp-minutes}") long expirationMinutes
  ) {
    this.algorithm = Algorithm.HMAC256(secret);
    this.expirationMinutes = expirationMinutes;
  }

  public String generateToken(UUID userId, String email, String displayName) {
    Instant now = Instant.now();
    Instant exp = now.plus(expirationMinutes, ChronoUnit.MINUTES);

    return JWT.create()
        .withSubject(userId.toString())
        .withClaim("email", email)
        .withClaim("name", displayName)
        .withIssuedAt(Date.from(now))
        .withExpiresAt(Date.from(exp))
        .sign(algorithm);
  }

  public DecodedJWT verify(String token) {
    return JWT.require(algorithm).build().verify(token);
  }

  public UUID getUserId(DecodedJWT jwt) {
    return UUID.fromString(jwt.getSubject());
  }

  public String getEmail(DecodedJWT jwt) {
    return jwt.getClaim("email").asString();
  }

  public String getName(DecodedJWT jwt) {
    return jwt.getClaim("name").asString();
  }
}
