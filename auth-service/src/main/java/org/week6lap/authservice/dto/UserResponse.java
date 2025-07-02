package org.week6lap.authservice.dto;

import org.week6lap.authservice.enums.UserRole;

public record UserResponse(
        Long id,
        String email,
        String username,
        UserRole role
) {}

