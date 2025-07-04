package org.week6lap.orderservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * DTO for creating a new order.
 */
public record OrderRequest(
        @NotNull(message = "Restaurant ID is required")
        Long restaurantId,

        @NotEmpty(message = "Order must contain at least one item")
        List<OrderItemRequest> items
) {}
