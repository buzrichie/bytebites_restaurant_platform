package org.week6lap.orderservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.week6lap.orderservice.dto.OrderRequest;
import org.week6lap.orderservice.dto.OrderResponse;
import org.week6lap.orderservice.service.OrderService;
import org.week6lap.orderservice.util.ResponseHelper;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "Endpoints for placing and viewing orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Place a new food order (only customers).
     */
    @PostMapping
    @Operation(summary = "Place a new order", responses = {
            @ApiResponse(responseCode = "201", description = "Order placed successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<?> placeOrder(
            @RequestBody @Valid OrderRequest orderRequest,
            Authentication authentication
    ) {
        String customerId = authentication.getName(); // Provided by JWT filter from gateway
        OrderResponse response = orderService.placeOrder(customerId, orderRequest);
        return ResponseHelper.success("Order placed successfully", response);
    }

    /**
     * Fetch all orders placed by the current customer.
     */
    @GetMapping("/my")
    @Operation(summary = "Get orders placed by the authenticated customer")
    public ResponseEntity<?> getMyOrders(Authentication authentication) {
        String customerId = authentication.getName();
        List<OrderResponse> orders = orderService.getOrdersByCustomer(customerId);
        return ResponseHelper.success("Your orders retrieved", orders);
    }

    /**
     * View a specific order by its ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseHelper.success("Order retrieved", response);
    }

    /**
     * For restaurant owners: view orders made to their restaurant.
     */
    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Get orders for a specific restaurant (for owners)")
    public ResponseEntity<?> getOrdersForRestaurant(@PathVariable Long restaurantId) {
        List<OrderResponse> orders = orderService.getOrdersForRestaurant(restaurantId);
        return ResponseHelper.success("Orders for restaurant retrieved", orders);
    }
}
