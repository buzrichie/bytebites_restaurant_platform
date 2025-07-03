package org.week6lap.restaurantservice.dto.menu;

import java.math.BigDecimal;

/**
 * Returned when viewing menu items.
 */
public record MenuItemResponse(
        Long id,
        String name,
        String description,
        BigDecimal price
) {}
