package org.week6lap.restaurantservice.dto.restaurant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.week6lap.restaurantservice.dto.menu.MenuItemRecord;

import java.util.List;

/**
 * Used for creating or updating a restaurant.
 */
public record RestaurantRecord(
        @NotBlank(message = "Restaurant name is required")
        @Size(min = 2, max = 150, message = "Restaurant name must be between 2 and 150 characters")
        String name,

        @NotBlank(message = "Address is required")
        String address,

        @NotBlank(message = "Phone number is required")
        String phone,

        @NotNull(message = "Owner ID is required")
        Long ownerId,

        // Optional list of menu items to save with the restaurant
        List<MenuItemRecord> menuItems
) {}

