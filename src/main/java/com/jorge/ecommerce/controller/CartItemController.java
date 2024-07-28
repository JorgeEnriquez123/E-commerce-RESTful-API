package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.CartItemDto;
import com.jorge.ecommerce.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartitem")
@RequiredArgsConstructor
@Validated
public class CartItemController {
    private final CartItemService cartItemService;

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItemDto>> findByCartId(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartItemService.getByCartId(cartId));
    }
}
