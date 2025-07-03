package org.week6lap.authservice.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for handling user login requests.
 */
public record AuthLoginRequestDTO(

        @Email(message = "Please provide a valid email")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        String password

) {}
