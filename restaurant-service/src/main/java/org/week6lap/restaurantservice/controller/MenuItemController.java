package org.week6lap.restaurantservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week6lap.restaurantservice.dto.menu.MenuItemRecord;
import org.week6lap.restaurantservice.dto.menu.MenuItemResponse;
import org.week6lap.restaurantservice.service.MenuItemService;
import org.week6lap.restaurantservice.util.ResponseHelper;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu-items")
@RequiredArgsConstructor
@Tag(name = "Menu Items", description = "CRUD operations for menu items within a restaurant")
public class MenuItemController {

    private final MenuItemService menuItemService;

    /**
     * Add a new menu item to a restaurant.
     */
    @PostMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Create a new menu item for a restaurant", responses = {
            @ApiResponse(responseCode = "201", description = "Menu item created"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not the owner"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public ResponseEntity<?> createMenuItem(
            @PathVariable Long restaurantId,
            @RequestHeader("X-USER-ID") String ownerId,
            @Valid @RequestBody MenuItemRecord record
    ) {
        var created = menuItemService.createMenuItem(restaurantId, record, Long.valueOf(ownerId));
        return ResponseHelper.success("Menu item created successfully", created);
    }

    /**
     * Get all menu items for a restaurant.
     */
    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Get all menu items for a restaurant", responses = {
            @ApiResponse(responseCode = "200", description = "List returned")
    })
    public ResponseEntity<?> getByRestaurant(@PathVariable Long restaurantId) {
        List<MenuItemResponse> items = menuItemService.getMenuItemsByRestaurant(restaurantId);
        return ResponseHelper.success("Menu items loaded", items);
    }

    /**
     * Update an existing menu item.
     */
    @PutMapping("/{itemId}")
    @Operation(summary = "Update a menu item", responses = {
            @ApiResponse(responseCode = "200", description = "Item updated"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not the owner"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public ResponseEntity<?> updateMenuItem(
            @PathVariable Long itemId,
            @RequestHeader("X-USER-ID") Long ownerId,
            @Valid @RequestBody MenuItemRecord record
    ) {
        var updated = menuItemService.updateMenuItem(itemId, record, ownerId);
        return ResponseHelper.success("Menu item updated", updated);
    }

    /**
     * Delete a menu item.
     */
    @DeleteMapping("/{itemId}")
    @Operation(summary = "Delete a menu item", responses = {
            @ApiResponse(responseCode = "204", description = "Item deleted"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not the owner"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public ResponseEntity<?> deleteMenuItem(
            @PathVariable Long itemId,
            @RequestHeader("X-USER-ID") String ownerId
    ) {
        menuItemService.deleteMenuItem(itemId, Long.valueOf(ownerId));
        return ResponseHelper.success("Menu item deleted", null);
    }
}

