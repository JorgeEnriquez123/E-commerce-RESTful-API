package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.CartDto;
import com.jorge.ecommerce.dto.CartItemDto;
import com.jorge.ecommerce.dto.create.CreateCartItemDto;
import com.jorge.ecommerce.dto.create.CreateCartDto;
import com.jorge.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @PostMapping("/{cartId}/item")
    public ResponseEntity<CartItemDto> addItemToCart(@PathVariable Long cartId, @RequestBody CreateCartItemDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(cartId, request));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<CartItemDto> updateItemQuantityFromCart(@PathVariable Long itemId, @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.updateItemQuantity(itemId, quantity));
    }
}
