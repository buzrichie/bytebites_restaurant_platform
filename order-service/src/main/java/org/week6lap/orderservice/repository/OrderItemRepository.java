package org.week6lap.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.week6lap.orderservice.model.OrderItem;

/**
 * Repository for managing OrderItem entities.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Currently no custom queries needed
}
