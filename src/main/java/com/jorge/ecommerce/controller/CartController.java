package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.CartDto;
import com.jorge.ecommerce.dto.CartItemDto;
import com.jorge.ecommerce.dto.create.CreateCartItemDto;
import com.jorge.ecommerce.model.Cart;
import com.jorge.ecommerce.model.CartItem;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.CartItemRepository;
import com.jorge.ecommerce.repository.CartRepository;
import com.jorge.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Min;
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
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @GetMapping("/items")
    public ResponseEntity<List<CartItemDto>> getCartItems(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getItemsFromUser(user));
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemDto> addItemToCart(@AuthenticationPrincipal User user, @RequestBody CreateCartItemDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItemToUserCart(user, request));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<Void> updateItemQuantityFromCart(@AuthenticationPrincipal User user,
                                                                  @PathVariable Long itemId,
                                                                  @Min(value = 1, message = "quantity must be greater then or equal to 1")
                                                                  @RequestParam Integer quantity) {
        cartService.updateItemQuantityFromUserCart(user, itemId, quantity);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItemFromCart(@AuthenticationPrincipal User user, @PathVariable Long itemId) {
        cartService.removeItem(user, itemId);
        return ResponseEntity.noContent().build();
    }
}
