package org.week6lap.restaurantservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.week6lap.restaurantservice.dto.menu.MenuItemRecord;
import org.week6lap.restaurantservice.dto.menu.MenuItemResponse;
import org.week6lap.restaurantservice.exception.ResourceNotFoundException;
import org.week6lap.restaurantservice.mapper.RestaurantMapper;
import org.week6lap.restaurantservice.model.MenuItem;
import org.week6lap.restaurantservice.model.Restaurant;
import org.week6lap.restaurantservice.repository.MenuItemRepository;
import org.week6lap.restaurantservice.repository.RestaurantRepository;
import org.week6lap.restaurantservice.service.MenuItemService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper mapper;

    /**
     * Add new menu item to a restaurant (only owners can add).
     */
    @Override
    @Transactional
    @CacheEvict(value = "menuItems", key = "'restaurant:' + #restaurantId")
    public MenuItemResponse createMenuItem(Long restaurantId, MenuItemRecord record, Long ownerId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (!restaurant.getOwnerId().equals(ownerId)) {
            throw new SecurityException("You do not own this restaurant");
        }

        MenuItem menuItem = mapper.toEntity(record);
        menuItem.setRestaurant(restaurant);

        return mapper.toResponse(menuItemRepository.save(menuItem));
    }

    /**
     * List all menu items for a restaurant.
     */
    @Override
    @Cacheable(value = "menuItems", key = "'restaurant:' + #restaurantId")
    public List<MenuItemResponse> getMenuItemsByRestaurant(Long restaurantId) {
        List<MenuItem> items = menuItemRepository.findByRestaurantId(restaurantId);
        return items.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    /**
     * Update menu item if the owner of the restaurant matches.
     */
    @Override
    @Transactional
    @CacheEvict(value = "menuItems", allEntries = true)
    public MenuItemResponse updateMenuItem(Long itemId, MenuItemRecord record, Long ownerId) {
        MenuItem existing = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        if (!existing.getRestaurant().getOwnerId().equals(ownerId)) {
            throw new SecurityException("You do not own this restaurant's menu item");
        }

        existing.setName(record.name());
        existing.setDescription(record.description());
        existing.setPrice(record.price());

        return mapper.toResponse(menuItemRepository.save(existing));
    }

    /**
     * Delete menu item if the owner matches.
     */
    @Override
    @Transactional
    @CacheEvict(value = "menuItems", allEntries = true)
    public void deleteMenuItem(Long itemId, Long ownerId) {
        MenuItem existing = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        if (!existing.getRestaurant().getOwnerId().equals(ownerId)) {
            throw new SecurityException("You are not authorized to delete this item");
        }

        menuItemRepository.deleteById(itemId);
    }
}
