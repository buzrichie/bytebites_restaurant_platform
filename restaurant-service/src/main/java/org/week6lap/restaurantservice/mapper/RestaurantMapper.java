package org.week6lap.restaurantservice.mapper;

import org.week6lap.restaurantservice.dto.menu.MenuItemRecord;
import org.week6lap.restaurantservice.dto.menu.MenuItemResponse;
import org.week6lap.restaurantservice.dto.restaurant.RestaurantRecord;
import org.week6lap.restaurantservice.dto.restaurant.RestaurantResponse;
import org.week6lap.restaurantservice.model.MenuItem;
import org.week6lap.restaurantservice.model.Restaurant;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    // ======= RESTAURANT =======

    Restaurant toEntity(RestaurantRecord dto);

    RestaurantResponse toResponse(Restaurant entity);


    // ======= MENU ITEMS =======

    MenuItem toEntity(MenuItemRecord dto);

    MenuItemResponse toResponse(MenuItem entity);

    List<MenuItemResponse> toResponseList(List<MenuItem> menuItems);

    List<MenuItem> toEntityList(List<MenuItemRecord> records);

    // Optional: Update restaurant entity with new data from DTO (for PUT)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRestaurantFromRecord(RestaurantRecord dto, @MappingTarget Restaurant entity);
}

