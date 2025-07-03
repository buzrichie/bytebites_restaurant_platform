package org.week6lap.authservice.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.week6lap.authservice.enums.UserRole;

public record UserRecord(

        Long id,

        @Email(message = "Please enter a valid email address")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Username is required")
        @Size(min = 2, max = 100, message = "Username must be 2–100 characters")
        String username,

        @NotBlank(message = "Password is required")
        String password,

        UserRole role

) {}

