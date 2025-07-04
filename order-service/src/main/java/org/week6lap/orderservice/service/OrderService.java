package org.week6lap.orderservice.service;

import org.week6lap.orderservice.dto.OrderRequest;
import org.week6lap.orderservice.dto.OrderResponse;

import java.util.List;

/**
 * Service interface for managing food orders.
 */
public interface OrderService {

    /**
     * Places a new order.
     */
    OrderResponse placeOrder(String customerId, OrderRequest orderRequest);

    /**
     * Retrieves all orders placed by a specific customer.
     */
    List<OrderResponse> getOrdersByCustomer(String customerId);

    /**
     * Retrieves all orders for a given restaurant.
     */
    List<OrderResponse> getOrdersForRestaurant(Long restaurantId);

    /**
     * Retrieves a specific order by ID.
     */
    OrderResponse getOrderById(Long id);
}
