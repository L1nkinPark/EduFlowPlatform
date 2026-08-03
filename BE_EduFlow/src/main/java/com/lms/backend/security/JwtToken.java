package com.lms.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtToken {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key signingKey;

    @PostConstruct
    void initializeSigningKey() {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException("JWT signing secret is not configured");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update("EduFlow JWT signing key\0".getBytes(StandardCharsets.UTF_8));
            signingKey = Keys.hmacShaKeyFor(digest.digest(secretKey.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is unavailable", ex);
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Tạo ra JWT từ thông tin user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities());
        return generateToken(claims, userDetails);
    }

    // Tạo ra JWT từ thông tin user kèm theo Claims
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, 1000L * 60 * 30);
    }

    private String buildToken(Map<String, Object> claims, UserDetails userDetails, long validityMillis) {
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername()) // Chủ đề (Subject)
                .setIssuedAt(new Date(currentTimeMillis)) // Thời gian phát hành (Issued At)
                .setExpiration(new Date(currentTimeMillis + validityMillis))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Ký JWT bằng SECRET_KEY
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenType", "refresh");
        return buildToken(claims, userDetails, 1000L * 60 * 60 * 24 * 7);
    }

    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(extraClaims);
        claims.put("tokenType", "refresh");
        return buildToken(claims, userDetails, 1000L * 60 * 60 * 24 * 7);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(extractClaim(token, claims -> claims.get("tokenType", String.class)));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Giải mã JWT Token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        if (signingKey == null) {
            initializeSigningKey();
        }
        return signingKey;
    }

}
