package org.week6lap.authservice.dto.auth;

/**
 * DTO for authentication response containing access token and user info.
 */
public record AuthResponseDTO(
        String accessToken,
        String tokenType,
        String username,
        String role
) {}
