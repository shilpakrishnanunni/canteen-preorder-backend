package org.example.CanteenPreorderBackend.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.CanteenPreorderBackend.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey secretKey;
    private final JwtParser jwtParser;
    private final long expiration;

    JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret)
        );
        this.jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .build();
        this.expiration = expiration;
    }

    public String generateToken(User user) {
        Date now = new Date();

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration))
                .signWith(secretKey)
                .compact();
    }

//    public String extractUserId(String token) {
//        return parseToken(token)
//                .getPayload()
//                .getSubject();
//    }
//
//    public String extractRole(String token) {
//        return parseToken(token)
//                .getPayload()
//                .get("role", String.class);
//    }
//
//    public boolean isValid(String token) {
//        try {
//            parseToken(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            return false;
//        }
//    }

    public Claims extractClaims(String token) {
        return jwtParser
                .parseSignedClaims(token)
                .getPayload();
    }

//    private Jws<Claims> parseToken(String token) {
//        return Jwts.parser()
//                .verifyWith(secretKey)
//                .build()
//                .parseSignedClaims(token);
//    }

}
