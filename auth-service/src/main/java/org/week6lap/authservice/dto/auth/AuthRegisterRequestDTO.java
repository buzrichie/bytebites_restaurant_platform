package org.week6lap.authservice.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for user registration requests.
 */
public record AuthRegisterRequestDTO(

        @NotBlank(message = "Name is required")
        String name,

        @Email(message = "Please provide a valid email")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        String password

) {}
