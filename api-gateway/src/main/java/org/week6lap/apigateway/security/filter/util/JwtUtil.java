package org.week6lap.apigateway.security.filter.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public Claims extractClaims(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT: " + e.getMessage());
        }
    }

    public String extractUserId(Claims claims) {
        return claims.getSubject(); // e.g., user ID
    }

    @SuppressWarnings("unchecked")
    public String extractRoles(Claims claims) {
        Object roles = claims.get("roles");
        if (roles instanceof List<?>) {
            return String.join(",", (List<String>) roles);
        } else if (roles instanceof String) {
            return (String) roles;
        }
        return "";
    }
}
