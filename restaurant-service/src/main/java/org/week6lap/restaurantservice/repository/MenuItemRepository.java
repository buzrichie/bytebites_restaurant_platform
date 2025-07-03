package org.week6lap.restaurantservice.repository;

import org.week6lap.restaurantservice.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    /**
     * Fetch all menu items for a specific restaurant.
     */
    List<MenuItem> findByRestaurantId(Long restaurantId);
}
