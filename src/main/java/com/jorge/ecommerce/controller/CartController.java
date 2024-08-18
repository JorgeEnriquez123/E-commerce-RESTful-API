package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.annotations.RoleAdminOrCustomer;
import com.jorge.ecommerce.dto.CartItemDto;
import com.jorge.ecommerce.dto.create.CreateCartItemDto;
import com.jorge.ecommerce.dto.update.UpdateCartItemDto;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.service.CartService;
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
@RequiredArgsConstructor
@RequestMapping(ApiRoutes.V1.Cart.ROOT)
@RoleAdminOrCustomer
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCartItems(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getItemsFromUser(user));
    }

    @PostMapping
    public ResponseEntity<CartItemDto> addItemToCart(@AuthenticationPrincipal User user, @RequestBody CreateCartItemDto createCartItemDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItemToUserCart(user, createCartItemDto));
    }

    @PatchMapping(ApiRoutes.V1.Cart.UPDATE_ITEM_FROM_CART)
    public ResponseEntity<Void> updateCartItemQuantityFromUserCart(@AuthenticationPrincipal User user, @PathVariable Long productId,
                                                                   @RequestBody UpdateCartItemDto updateCartItemDto) {
        cartService.updateItemQuantityFromUserCart(user, productId, updateCartItemDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ApiRoutes.V1.Cart.REMOVE_ITEM_FROM_CART)
    public ResponseEntity<Void> removeItemFromCart(@AuthenticationPrincipal User user, @PathVariable Long productId) {
        cartService.removeItem(user, productId);
        return ResponseEntity.noContent().build();
    }
}
