package org.week6lap.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for each item in an order.
 */
public record OrderItemRequest(
        @NotNull(message = "Menu item ID is required")
        Long menuItemId,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity
) {}
