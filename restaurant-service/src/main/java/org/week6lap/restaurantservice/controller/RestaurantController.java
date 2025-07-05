package org.week6lap.restaurantservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week6lap.restaurantservice.dto.restaurant.RestaurantRecord;
import org.week6lap.restaurantservice.dto.restaurant.RestaurantResponse;
import org.week6lap.restaurantservice.service.RestaurantService;
import org.week6lap.restaurantservice.util.ResponseHelper;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurant Management", description = "Operations related to restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    /**
     * Register a new restaurant by an owner.
     */
    @PostMapping
    @Operation(summary = "Create a new restaurant", responses = {
            @ApiResponse(responseCode = "201", description = "Restaurant created"),
            @ApiResponse(responseCode = "403", description = "Forbidden - duplicate name or not allowed")
    })
    public ResponseEntity<?> createRestaurant(
            @RequestHeader("X-USER-ID") String ownerId,
            @Valid @RequestBody RestaurantRecord record
    ) {
        var created = restaurantService.createRestaurant(record);
        return ResponseHelper.success("Restaurant created successfully", created);
    }

    /**
     * Get all restaurants (open to all authenticated users).
     */
    @GetMapping
    @Operation(summary = "List all restaurants", responses = {
            @ApiResponse(responseCode = "200", description = "Restaurants listed")
    })
    public ResponseEntity<?> getAllRestaurants() {
        List<RestaurantResponse> restaurants = restaurantService.getAllRestaurants();
        return ResponseHelper.success("All restaurants fetched", restaurants);
    }

    /**
     * Get restaurant by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get restaurant by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Restaurant found"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public ResponseEntity<?> getRestaurantById(@PathVariable Long id) {
        var restaurant = restaurantService.getRestaurantById(id);
        return ResponseHelper.success("Restaurant fetched", restaurant);
    }

    /**
     * Update a restaurant's data (owner only).
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update restaurant (owner only)", responses = {
            @ApiResponse(responseCode = "200", description = "Restaurant updated"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not owner"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public ResponseEntity<?> updateRestaurant(
            @PathVariable Long id,
            @RequestHeader("X-USER-ID") String ownerId,
            @Valid @RequestBody RestaurantRecord record
    ) {
        var updated = restaurantService.updateRestaurant(id, record);
        return ResponseHelper.success("Restaurant updated", updated);
    }

    /**
     * Delete a restaurant (only owner).
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete restaurant (owner only)", responses = {
            @ApiResponse(responseCode = "204", description = "Restaurant deleted"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not owner"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public ResponseEntity<?> deleteRestaurant(
            @PathVariable Long id,
            @RequestHeader("X-USER-ID") String ownerId
    ) {
        restaurantService.deleteRestaurant(id, Long.valueOf(ownerId));
        return ResponseHelper.success("Restaurant deleted", null);
    }
}

