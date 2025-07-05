package org.week6lap.orderservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.week6lap.orderservice.dto.OrderRequest;
import org.week6lap.orderservice.dto.OrderResponse;
import org.week6lap.orderservice.event.OrderPlacedEvent;
import org.week6lap.orderservice.event.publisher.OrderEventPublisher;
import org.week6lap.orderservice.exception.ResourceNotFoundException;
import org.week6lap.orderservice.mapper.OrderMapper;
import org.week6lap.orderservice.model.Order;
import org.week6lap.orderservice.model.OrderItem;
import org.week6lap.orderservice.repository.OrderRepository;
import org.week6lap.orderservice.service.OrderService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for order management.
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderEventPublisher orderEventPublisher;

    /**
     * Places a new food order.
     */
    @Override
    @Transactional
    public OrderResponse placeOrder(String customerId, OrderRequest orderRequest) {
        Order order = orderMapper.toEntity(orderRequest, customerId);
        Order saved = orderRepository.save(order);
        // Build and emit event
        OrderPlacedEvent event = OrderPlacedEvent.builder()
                .orderId(Long.valueOf(saved.getId().toString()))
                .restaurantId(order.getRestaurantId())
                .userId(Long.valueOf(order.getCustomerId()))
                .status(String.valueOf(order.getStatus()))
                .build();

        System.out.println(event);

        orderEventPublisher.publishOrderPlacedEvent(event);

        return orderMapper.toResponse(saved);
    }

    /**
     * Retrieves orders placed by a specific customer.
     */
    @Override
    public List<OrderResponse> getOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all orders for a restaurant.
     */
    @Override
    public List<OrderResponse> getOrdersForRestaurant(Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific order by its ID.
     */
    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with ID " + id + " not found"));
        return orderMapper.toResponse(order);
    }
}
