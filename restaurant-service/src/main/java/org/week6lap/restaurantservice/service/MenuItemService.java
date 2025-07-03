package org.week6lap.restaurantservice.service;

import org.week6lap.restaurantservice.dto.menu.MenuItemRecord;
import org.week6lap.restaurantservice.dto.menu.MenuItemResponse;

import java.util.List;

public interface MenuItemService {

    MenuItemResponse createMenuItem(Long restaurantId, MenuItemRecord record, Long ownerId);

    List<MenuItemResponse> getMenuItemsByRestaurant(Long restaurantId);

    MenuItemResponse updateMenuItem(Long itemId, MenuItemRecord record, Long ownerId);

    void deleteMenuItem(Long itemId, Long ownerId);
}
