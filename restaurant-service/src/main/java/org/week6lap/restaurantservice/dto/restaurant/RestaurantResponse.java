package org.week6lap.restaurantservice.dto.restaurant;

import org.week6lap.restaurantservice.dto.menu.MenuItemResponse;

import java.util.List;

/**
 * Returned when fetching restaurant data.
 */
public record RestaurantResponse(
        Long id,
        String name,
        String address,
        String phone,
        Long ownerId,
        List<MenuItemResponse> menuItems
) {}
