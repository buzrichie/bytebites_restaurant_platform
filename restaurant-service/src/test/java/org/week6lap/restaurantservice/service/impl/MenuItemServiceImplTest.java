package org.week6lap.restaurantservice.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.week6lap.restaurantservice.dto.menu.MenuItemRecord;
import org.week6lap.restaurantservice.dto.menu.MenuItemResponse;
import org.week6lap.restaurantservice.exception.ResourceNotFoundException;
import org.week6lap.restaurantservice.mapper.RestaurantMapper;
import org.week6lap.restaurantservice.model.MenuItem;
import org.week6lap.restaurantservice.model.Restaurant;
import org.week6lap.restaurantservice.repository.MenuItemRepository;
import org.week6lap.restaurantservice.repository.RestaurantRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuItemServiceImplTest {

    private MenuItemRepository menuItemRepository;
    private RestaurantRepository restaurantRepository;
    private RestaurantMapper mapper;
    private MenuItemServiceImpl service;

    @BeforeEach
    void setUp() {
        menuItemRepository = mock(MenuItemRepository.class);
        restaurantRepository = mock(RestaurantRepository.class);
        mapper = mock(RestaurantMapper.class);
        service = new MenuItemServiceImpl(menuItemRepository, restaurantRepository, mapper);
    }

    @Test
    void testCreateMenuItem_Success() {
        Long restaurantId = 1L;
        Long ownerId = 99L;
        MenuItemRecord record = new MenuItemRecord("Jollof", "Spicy rice", new BigDecimal("20.0"));
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setOwnerId(ownerId);

        MenuItem menuItem = new MenuItem();
        menuItem.setName("Jollof");
        menuItem.setDescription("Spicy rice");
        menuItem.setPrice(new BigDecimal("20.0"));
        menuItem.setRestaurant(restaurant);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(mapper.toEntity(record)).thenReturn(menuItem);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);
        when(mapper.toResponse(menuItem)).thenReturn(new MenuItemResponse(1L, "Jollof", "Spicy rice", new BigDecimal("20.0")));

        MenuItemResponse response = service.createMenuItem(restaurantId, record, ownerId);

        assertEquals("Jollof", response.name());
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    void testCreateMenuItem_RestaurantNotFound() {
        Long restaurantId = 1L;
        Long ownerId = 99L;
        MenuItemRecord record = new MenuItemRecord("Jollof", "Spicy rice", new BigDecimal("20.0"));

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                service.createMenuItem(restaurantId, record, ownerId));
    }

    @Test
    void testCreateMenuItem_UnauthorizedOwner() {
        Long restaurantId = 1L;
        Long ownerId = 99L;
        Long wrongOwnerId = 55L;
        MenuItemRecord record = new MenuItemRecord("Jollof", "Spicy rice", new BigDecimal("20.0"));

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setOwnerId(ownerId); // actual owner is 99

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        assertThrows(SecurityException.class, () ->
                service.createMenuItem(restaurantId, record, wrongOwnerId));
    }
}
