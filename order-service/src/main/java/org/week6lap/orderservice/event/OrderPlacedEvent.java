package org.week6lap.orderservice.event;

import lombok.Builder;

@Builder
public record OrderPlacedEvent(
        Long orderId,
        Long userId,
        Long restaurantId,
        String status
){}
