package com.fixnow.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // !!! IMPORTANT: Use a strong, securely stored secret key in production! Load from config. !!!
    // For demo purposes, using a temporary key gen erated on startup:
    @SuppressWarnings("deprecation") // Suppress warning for intended use
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Reverted to original
    private final long expirationMs = 86400000; // 24 hours

    public String generateToken(String username, Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .claim(Claims.SUBJECT, username)
                .claim(Claims.ISSUED_AT, now)
                .claim(Claims.EXPIRATION, expiryDate)
                .claim("userId", userId)
                .signWith(secretKey)
                .compact();
    }

    // You can add methods for validation and claims extraction later if needed
    /*
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Log exception: MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException
            return false;
        }
    }
    */
} 