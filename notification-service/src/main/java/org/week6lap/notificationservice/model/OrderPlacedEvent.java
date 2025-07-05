package org.week6lap.notificationservice.model;

public record OrderPlacedEvent(
        Long orderId,
        Long userId,
        Long restaurantId,
        String status
) {}
