package org.week6lap.orderservice.mapper;

import org.springframework.stereotype.Component;
import org.week6lap.orderservice.dto.*;
import org.week6lap.orderservice.enums.OrderStatus;
import org.week6lap.orderservice.model.Order;
import org.week6lap.orderservice.model.OrderItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts between Order-related DTOs and entities.
 */
@Component
public class OrderMapper {

    /**
     * Converts an OrderRequest DTO to an Order entity.
     * Note: customerId should be injected from context before saving.
     */
    public Order toEntity(OrderRequest request, String customerId) {
        Order order = Order.builder()
                .customerId(customerId)
                .restaurantId(request.restaurantId())
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        List<OrderItem> items = request.items().stream()
                .map(itemReq -> OrderItem.builder()
                        .menuItemId(itemReq.menuItemId())
                        .quantity(itemReq.quantity())
                        .order(order)  // set back-reference for cascading
                        .build())
                .collect(Collectors.toList());

        order.setItems(items);
        return order;
    }

    /**
     * Converts an Order entity to OrderResponse DTO.
     */
    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getId(),
                        item.getMenuItemId(),
                        item.getQuantity()))
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getRestaurantId(),
                order.getStatus(),
                order.getCreatedAt(),
                items
        );
    }
}
