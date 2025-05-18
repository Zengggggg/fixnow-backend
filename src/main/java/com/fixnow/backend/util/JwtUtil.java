package com.fixnow.backend.util;

import com.fixnow.backend.models.User;
import com.fixnow.backend.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.StringJoiner;

@Component
public class JwtUtil {

    // !!! IMPORTANT: Use a strong, securely stored secret key in production! Load from config. !!!
    // For demo purposes, using a temporary key gen erated on startup:
    @SuppressWarnings("deprecation") // Suppress warning for intended use
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Reverted to original
    private final long expirationMs = 86400000; // 24 hours
    private final UserRepository userRepository;

    public JwtUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Jwt jwt = (Jwt) authentication.getPrincipal();
        Map<String, Object> user = jwt.getClaim("user");
        return Long.valueOf(user.get("id").toString()); // Retrieve userId directly
    }


    public String generateToken(String username, Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);
        User user = userRepository.findById(userId).orElseThrow();
        return Jwts.builder()
                .claim(Claims.SUBJECT, username)
                .claim(Claims.ISSUED_AT, now)
                .claim(Claims.EXPIRATION, expiryDate)
                .claim("userId", userId)
                .claim("scopes", buildScope(user))
                .signWith(secretKey)
                .compact();
    }

    private String buildScope(User user) {
        StringJoiner sj = new StringJoiner(" ");
        sj.add(user.getRole().toString());
        return sj.toString();
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
            // System.err.println("JWT validation error: " + e.getMessage()); // Added simple error logging
            return false;
        }
    }
    */
} 