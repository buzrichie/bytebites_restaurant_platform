package org.week6lap.apigateway.security.filter;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import org.week6lap.apigateway.security.filter.util.JwtUtil;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    @Value("${internal.secret}")
    private String internalSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Bypass public routes (e.g., login/register)
        String path = exchange.getRequest().getURI().getPath();
        if (path.contains("/api/v1/auth/login") || path.contains("/api/v1/auth/register")) {
            return chain.filter(exchange);
        }

        // Get Authorization header
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            String token = authHeader.substring(7); // Remove "Bearer "
            Claims claims = jwtUtil.extractClaims(token);

            String userId = jwtUtil.extractUserId(claims);
            String roles = jwtUtil.extractRoles(claims);

            // Add headers to propagate identity
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-USER-ID", userId)
                    .header("X-USER-ROLE", roles)
                    .header("X-Internal-Auth", internalSecret) // For internal validation
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception ex) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1; // Run early
    }
}
