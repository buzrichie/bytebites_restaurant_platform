package org.week6lap.authservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week6lap.authservice.dto.auth.AuthLoginRequestDTO;
import org.week6lap.authservice.dto.auth.AuthRegisterRequestDTO;
import org.week6lap.authservice.dto.auth.AuthResponseDTO;
import org.week6lap.authservice.dto.user.UserResponse;
import org.week6lap.authservice.service.AuthService;
import org.week6lap.authservice.util.ResponseHelper;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login & Registration APIs")
public class AuthController {

    private final AuthService authService;

    /**
     * Handles user login and returns JWT access token.
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user with email and password")
    public ResponseEntity<?> login(@RequestBody @Valid AuthLoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        return ResponseHelper.success("Login successful", response);
    }

    /**
     * Handles user registration.
     */
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Registers a user with default CUSTOMER role")
    public ResponseEntity<?> register(@RequestBody @Valid AuthRegisterRequestDTO request) {
        UserResponse response = authService.register(request);
        return ResponseHelper.success("Registration successful", response);
    }
}
