package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.annotations.RoleAdmin;
import com.jorge.ecommerce.annotations.RoleAdminOrCustomer;
import com.jorge.ecommerce.dto.OrderDto;
import com.jorge.ecommerce.dto.create.CreateOrderDto;
import com.jorge.ecommerce.dto.update.UpdateOrderStatusDto;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(
        name = "bearerAuth"
)
@RestController
@RequestMapping(ApiRoutes.V1.Order.ROOT)
@RequiredArgsConstructor
@RoleAdminOrCustomer
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.getOrdersWithDetailsFromUser(user));
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@AuthenticationPrincipal User user, @RequestBody CreateOrderDto createOrderDto) {
        orderService.createOrder(user, createOrderDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RoleAdmin
    @PatchMapping(ApiRoutes.V1.Order.PUT_UPDATE_STATUS)
    public ResponseEntity<Void> updateOrderStatus(@AuthenticationPrincipal User user,
                                                  @PathVariable Long orderId, @RequestBody UpdateOrderStatusDto updateOrderStatusDto) {
        orderService.updatedOrderStatus(user, orderId, updateOrderStatusDto);
        return ResponseEntity.noContent().build();
    }
}
