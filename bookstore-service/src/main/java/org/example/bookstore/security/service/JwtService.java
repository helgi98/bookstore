package org.example.bookstore.security.service;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.security.model.UserSecurityDetails;
import org.example.bookstore.security.property.JwtPropConfig;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtPropConfig jwtPropConfig;

    private final SecretKey signingKey;

    public String generateToken(UserSecurityDetails userDetails) {
        return Jwts.builder()
            .subject(userDetails.userName())
            .issuedAt(new Date(Instant.now().toEpochMilli()))
            .expiration(new Date(Instant.now().plus(jwtPropConfig.ttl(), ChronoUnit.MILLIS).toEpochMilli()))
            .issuer(jwtPropConfig.issuer())
            .signWith(signingKey)
            .claim("userId", userDetails.userId())
            .compact();
    }

}
