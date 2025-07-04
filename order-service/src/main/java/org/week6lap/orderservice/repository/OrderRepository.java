package org.week6lap.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.week6lap.orderservice.model.Order;

import java.util.List;

/**
 * Repository for managing Order entities.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Fetch all orders placed by a specific customer
    List<Order> findByCustomerId(String customerId);

    // Fetch all orders for a specific restaurant
    List<Order> findByRestaurantId(Long restaurantId);
}
