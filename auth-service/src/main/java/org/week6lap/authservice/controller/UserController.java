package org.week6lap.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week6lap.authservice.dto.UserRecord;
import org.week6lap.authservice.dto.UserResponse;
import org.week6lap.authservice.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Operations related to user accounts")
public class UserController {

    private final UserService userService;

    /**
     * Create a new user
     */
    @PostMapping
    @Operation(summary = "Create a new user", responses = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRecord userRecord) {
        UserResponse created = userService.createUser(userRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get a single user by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Fetch user by ID", responses = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Get all users
     */
    @GetMapping
    @Operation(summary = "Fetch all users", responses = {
            @ApiResponse(responseCode = "200", description = "Users listed")
    })
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Update an existing user
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update user by ID", responses = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserRecord userRecord
    ) {
        UserResponse updated = userService.updateUser(id, userRecord);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a user
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID", responses = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
