package org.week6lap.restaurantservice.service;

import org.week6lap.restaurantservice.dto.restaurant.RestaurantRecord;
import org.week6lap.restaurantservice.dto.restaurant.RestaurantResponse;

import java.util.List;

public interface RestaurantService {

    RestaurantResponse createRestaurant(RestaurantRecord record);

    RestaurantResponse getRestaurantById(Long id);

    List<RestaurantResponse> getAllRestaurants();

    List<RestaurantResponse> getRestaurantsByOwner(Long ownerId);

    RestaurantResponse updateRestaurant(Long id, RestaurantRecord updated);

    void deleteRestaurant(Long id, Long ownerId); // Only owner or admin can delete
}

