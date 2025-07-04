package org.week6lap.orderservice.dto;

import org.week6lap.orderservice.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO representing an order's state.
 */
public record OrderResponse(
        Long id,
        String customerId,
        Long restaurantId,
        OrderStatus status,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {}
