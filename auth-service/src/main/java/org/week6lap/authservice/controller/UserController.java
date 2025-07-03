package org.week6lap.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week6lap.authservice.dto.user.UserRecord;
import org.week6lap.authservice.dto.user.UserResponse;
import org.week6lap.authservice.service.UserService;
import org.week6lap.authservice.util.ResponseHelper;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
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
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRecord userRecord) {
        UserResponse created = userService.createUser(userRecord);
        return ResponseHelper.success("User created successfully", created);
    }

    /**
     * Get a single user by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Fetch user by ID", responses = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseHelper.success("User retrieved successfully", user);
    }

    /**
     * Get all users
     */
    @GetMapping
    @Operation(summary = "Fetch all users", responses = {
            @ApiResponse(responseCode = "200", description = "Users listed")
    })
    public ResponseEntity<?> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseHelper.success("All users retrieved", users);
    }

    /**
     * Update an existing user
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update user by ID", responses = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserRecord userRecord
    ) {
        UserResponse updated = userService.updateUser(id, userRecord);
        return ResponseHelper.success("User updated successfully", updated);
    }

    /**
     * Delete a user
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID", responses = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseHelper.success("User deleted successfully", null);
    }
}
