package org.week6lap.restaurantservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.week6lap.restaurantservice.dto.restaurant.RestaurantRecord;
import org.week6lap.restaurantservice.dto.restaurant.RestaurantResponse;
import org.week6lap.restaurantservice.exception.ResourceNotFoundException;
import org.week6lap.restaurantservice.mapper.RestaurantMapper;
import org.week6lap.restaurantservice.model.Restaurant;
import org.week6lap.restaurantservice.repository.RestaurantRepository;
import org.week6lap.restaurantservice.service.RestaurantService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper mapper;

    /**
     * Create a new restaurant and map the response.
     */
    @Override
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public RestaurantResponse createRestaurant(RestaurantRecord record) {
        Restaurant restaurant = mapper.toEntity(record);
        return mapper.toResponse(restaurantRepository.save(restaurant));
    }

    /**
     * Retrieve a restaurant by ID.
     */
    @Override
    @Cacheable(value = "restaurants", key = "#id")
    public RestaurantResponse getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant with ID " + id + " not found"));
        return mapper.toResponse(restaurant);
    }

    /**
     * Get all restaurants in the system.
     */
    @Override
    @Cacheable(value = "restaurants", key = "'all'")
    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all restaurants owned by a specific user.
     */
    @Override
    @Cacheable(value = "restaurants", key = "'owner:' + #ownerId")
    public List<RestaurantResponse> getRestaurantsByOwner(Long ownerId) {
        return restaurantRepository.findByOwnerId(ownerId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update a restaurant (only if owned by the same user).
     */
    @Override
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public RestaurantResponse updateRestaurant(Long id, RestaurantRecord updated) {
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant with ID " + id + " not found"));

        if (!existing.getOwnerId().equals(updated.ownerId())) {
            throw new SecurityException("You do not have permission to update this restaurant");
        }

        mapper.updateRestaurantFromRecord(updated, existing);

        return mapper.toResponse(restaurantRepository.save(existing));
    }

    /**
     * Delete a restaurant only if the owner matches (or elevate with admin later).
     */
    @Override
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public void deleteRestaurant(Long id, Long ownerId) {
        if (!restaurantRepository.existsByIdAndOwnerId(id, ownerId)) {
            throw new SecurityException("You are not authorized to delete this restaurant");
        }
        restaurantRepository.deleteById(id);
    }
}

