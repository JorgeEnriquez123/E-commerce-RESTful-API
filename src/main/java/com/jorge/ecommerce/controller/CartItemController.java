package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.CartItemDto;
import com.jorge.ecommerce.dto.create.CreateCartItemDto;
import com.jorge.ecommerce.service.CartItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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

    @PostMapping
    public ResponseEntity<CartItemDto> saveNewItemtoCart(@Valid @RequestBody CreateCartItemDto createCartItemDto) {
        return ResponseEntity.ok(cartItemService.saveNewItemToCart(createCartItemDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItemDto> updateItemQuantityByCartItemId(@PathVariable Long id,
                                                                  @RequestParam @Min(value = 1, message = "quantity must be greater then or equal to 1")
                                                                  Integer quantity){
        return ResponseEntity.ok(cartItemService.updateItemQuantityByCartItemId(id, quantity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemByCartItemId(@PathVariable Long id){
        cartItemService.removeItemFromCartByCartItemId(id);
        return ResponseEntity.noContent().build();
    }
}
