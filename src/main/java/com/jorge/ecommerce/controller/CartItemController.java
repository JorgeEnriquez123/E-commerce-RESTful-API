package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.CartItemDto;
import com.jorge.ecommerce.dto.create.CreateCartItemDto;
import com.jorge.ecommerce.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartitem")
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItemDto>> findByCartId(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartItemService.findByCartId(cartId));
    }

    @PostMapping
    public ResponseEntity<CartItemDto> saveNewItemtoCart(@Valid @RequestBody CreateCartItemDto createCartItemDto) {
        return ResponseEntity.ok(cartItemService.saveNewItemToCart(createCartItemDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItemDto> updateItemQuantityByCartId(@PathVariable Long id, @RequestParam Integer quantity){
        return ResponseEntity.ok(cartItemService.updateItemQuantityByCartId(id, quantity));
    }
}
