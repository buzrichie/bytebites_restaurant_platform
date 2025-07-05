package org.week6lap.orderservice.dto;

/**
 * Response DTO for each item in an order.
 */
public record OrderItemResponse(
        Long id,
        Long menuItemId,
        int quantity
) {}
