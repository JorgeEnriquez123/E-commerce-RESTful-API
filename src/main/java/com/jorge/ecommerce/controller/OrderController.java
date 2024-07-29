package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.OrderDto;
import com.jorge.ecommerce.dto.create.CreateOrderDto;
import com.jorge.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderDto createOrderDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(createOrderDto));
    }
}
