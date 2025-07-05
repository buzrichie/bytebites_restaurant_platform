package org.week6lap.restaurantservice.event.model;

public record OrderPlacedEvent(
        Long orderId,
        Long userId,
        Long restaurantId,
        String status
) {}
