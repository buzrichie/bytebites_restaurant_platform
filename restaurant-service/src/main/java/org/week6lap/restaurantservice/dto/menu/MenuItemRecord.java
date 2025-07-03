package org.week6lap.restaurantservice.dto.menu;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Used to create or update a menu item.
 */
public record MenuItemRecord(
        @NotBlank(message = "Item name is required")
        String name,

        @NotBlank(message = "Item description is required")
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.1", message = "Price must be greater than 0")
        BigDecimal price
) {}
