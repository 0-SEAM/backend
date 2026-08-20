package com.seam.security;

import com.seam.entity.UserAccount;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private static final int MIN_SECRET_BYTES = 32;

    private final String secret;
    private final long accessExpirationMs;
    private final long refreshExpirationMsValue;

    private SecretKey signingKey;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-expiration-ms}") long accessExpirationMs,
            @Value("${app.jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.secret = secret;
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMsValue = refreshExpirationMs;
    }

    @PostConstruct
    public void init() {
        if (secret.getBytes(StandardCharsets.UTF_8).length < MIN_SECRET_BYTES) {
            throw new IllegalArgumentException("JWT secret must be at least 32 bytes");
        }
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(UserAccount user) {
        return createToken(user, "ACCESS", accessExpirationMs);
    }

    public String createRefreshToken(UserAccount user) {
        return createToken(user, "REFRESH", refreshExpirationMsValue);
    }

    public boolean isAccessToken(String token) {
        return "ACCESS".equals(parse(token).get("tokenType", String.class));
    }

    public boolean isRefreshToken(String token) {
        return "REFRESH".equals(parse(token).get("tokenType", String.class));
    }

    public String getUserId(String token) {
        return parse(token).getSubject();
    }

    public String getRole(String token) {
        return parse(token).get("role", String.class);
    }

    public long refreshExpirationMs() {
        return refreshExpirationMsValue;
    }

    private String createToken(UserAccount user, String tokenType, long expirationMs) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.getUserId())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .claim("tokenType", tokenType)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMs)))
                .signWith(signingKey)
                .compact();
    }

    private Claims parse(String token) {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    }
}
