package com.anffercastillo.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtils {
  private String secret = "Secret";

  public String generateToken(UserDetails user) {

    return JWT.create()
        .withExpiresAt(new Date(System.currentTimeMillis() + (10 * 3600 * 1000)))
        .withSubject(user.getUsername())
        .withIssuedAt(new Date(System.currentTimeMillis()))
        .sign(Algorithm.HMAC256(secret));
  }

  public Optional<DecodedJWT> getDecodedToken(String token) {
    var verifier = JWT.require(Algorithm.HMAC256(secret)).build();
    var decodedToken = verifier.verify(token);
    var currentDate = new Date(System.currentTimeMillis());
    var expiresAt = decodedToken.getExpiresAt();
    if (expiresAt.after(currentDate)) {
      return Optional.of(decodedToken);
    }
    return Optional.empty();
  }
}
