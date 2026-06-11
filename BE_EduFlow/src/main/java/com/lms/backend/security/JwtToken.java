package com.lms.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtToken {

    private static final String SECRET_KEY = "498299AF559B27B16323E8187289FFD95CF9E6BEA9F143C209C2A57291D38DD5";

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
        // Thời gian hiện tại
        long currentTimeMillis = System.currentTimeMillis();

        // Thời gian hết hạn của Token
        long expirationTimeMillis = currentTimeMillis + (1000 * 60 * 30); // 30 phút

        // Tạo JWT Token
        return Jwts.builder()
                .setClaims(extraClaims) // Claims (nội dung)
                .setSubject(userDetails.getUsername()) // Chủ đề (Subject)
                .setIssuedAt(new Date(currentTimeMillis)) // Thời gian phát hành (Issued At)
                .setExpiration(new Date(expirationTimeMillis)) // Thời gian hết hạn (Expiration)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Ký JWT bằng SECRET_KEY
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails);
    }

    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return generateToken(extraClaims, userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
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
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}