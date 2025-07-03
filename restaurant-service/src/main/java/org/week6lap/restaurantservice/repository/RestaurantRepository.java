package org.week6lap.restaurantservice.repository;

import org.week6lap.restaurantservice.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    /**
     * Find all restaurants owned by a specific user.
     */
    List<Restaurant> findByOwnerId(Long ownerId);

    /**
     * Check if a specific restaurant is owned by a user (for ownership enforcement).
     */
    boolean existsByIdAndOwnerId(Long id, Long ownerId);
}

