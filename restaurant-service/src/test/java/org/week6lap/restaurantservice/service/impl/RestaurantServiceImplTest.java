package org.week6lap.restaurantservice.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.week6lap.restaurantservice.dto.restaurant.RestaurantRecord;
import org.week6lap.restaurantservice.dto.restaurant.RestaurantResponse;
import org.week6lap.restaurantservice.exception.ResourceNotFoundException;
import org.week6lap.restaurantservice.mapper.RestaurantMapper;
import org.week6lap.restaurantservice.model.Restaurant;
import org.week6lap.restaurantservice.repository.RestaurantRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private RestaurantRecord record;
    private Restaurant restaurant;
    private RestaurantResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        record = new RestaurantRecord(
                "Test Restaurant",
                "123 Main St",
                "+233500000000",
                1L,
                null
        );

        restaurant = new Restaurant(1L, "Test Restaurant", "123 Main St", "+233500000000", 1L, null);
        response = new RestaurantResponse(1L, "Test Restaurant", "123 Main St", "+233500000000", 1L, null);
    }

    @Test
    void createRestaurant_success() {
        when(restaurantMapper.toEntity(record)).thenReturn(restaurant);
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        when(restaurantMapper.toResponse(restaurant)).thenReturn(response);

        RestaurantResponse result = restaurantService.createRestaurant(record);

        assertEquals("Test Restaurant", result.name());
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void getRestaurantById_found() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantMapper.toResponse(restaurant)).thenReturn(response);

        RestaurantResponse result = restaurantService.getRestaurantById(1L);

        assertEquals(1L, result.id());
    }

    @Test
    void getRestaurantById_notFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.getRestaurantById(99L));
    }

    @Test
    void getAllRestaurants_success() {
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));
        when(restaurantMapper.toResponse(restaurant)).thenReturn(response);

        List<RestaurantResponse> result = restaurantService.getAllRestaurants();

        assertEquals(1, result.size());
    }

    @Test
    void getRestaurantsByOwner_success() {
        when(restaurantRepository.findByOwnerId(1L)).thenReturn(List.of(restaurant));
        when(restaurantMapper.toResponse(restaurant)).thenReturn(response);

        List<RestaurantResponse> result = restaurantService.getRestaurantsByOwner(1L);

        assertEquals(1, result.size());
    }

    @Test
    void updateRestaurant_success() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        doNothing().when(restaurantMapper).updateRestaurantFromRecord(record, restaurant);
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        when(restaurantMapper.toResponse(restaurant)).thenReturn(response);

        RestaurantResponse result = restaurantService.updateRestaurant(1L, record);

        assertEquals("Test Restaurant", result.name());
    }

    @Test
    void updateRestaurant_wrongOwner_throwsSecurityException() {
        Restaurant someoneElse = new Restaurant(1L, "Other", "XYZ", "+111", 999L, null);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(someoneElse));

        assertThrows(SecurityException.class, () -> restaurantService.updateRestaurant(1L, record));
    }

    @Test
    void deleteRestaurant_success() {
        when(restaurantRepository.existsByIdAndOwnerId(1L, 1L)).thenReturn(true);
        doNothing().when(restaurantRepository).deleteById(1L);

        restaurantService.deleteRestaurant(1L, 1L);

        verify(restaurantRepository).deleteById(1L);
    }

    @Test
    void deleteRestaurant_wrongOwner_throwsSecurityException() {
        when(restaurantRepository.existsByIdAndOwnerId(1L, 1L)).thenReturn(false);

        assertThrows(SecurityException.class, () -> restaurantService.deleteRestaurant(1L, 1L));
    }
}
