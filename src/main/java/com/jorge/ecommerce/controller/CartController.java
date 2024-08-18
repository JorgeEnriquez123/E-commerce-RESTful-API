package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.annotations.RoleAdminOrCustomer;
import com.jorge.ecommerce.dto.CartItemDto;
import com.jorge.ecommerce.dto.create.CreateCartItemDto;
import com.jorge.ecommerce.dto.update.UpdateCartItemDto;
import com.jorge.ecommerce.handler.response.ErrorResponse;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Carts", description = "User's cart operations")
public class CartController {
    private final CartService cartService;

    @Operation(summary = "Get all Items from current User's cart")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Successful operation", content = @Content(
                            schema = @Schema(implementation = CartItemDto.class)
                    )),
                    @ApiResponse(
                            responseCode = "401", description = "The User is not authorized to access this", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "403", description = "The User has insufficient permission to access this", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "500", description = "There was an internal server error", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
            }
    )
    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCartItems(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getItemsFromUser(user));
    }

    @Operation(summary = "Add a new Item to the current User's cart")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201", description = "Resource successfully created", content = @Content(
                            schema = @Schema(implementation = CartItemDto.class)
                    )),
                    @ApiResponse(
                            responseCode = "400", description = "There was a problem with the request. One or more parameters failed some validations", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "404", description = "A certain resource has not been found", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "401", description = "The User is not authorized to perform this operation", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "403", description = "The User has insufficient permission to perform this operation", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "500", description = "There was an internal server error", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
            }
    )
    @PostMapping
    public ResponseEntity<CartItemDto> addItemToCart(@AuthenticationPrincipal User user, @RequestBody CreateCartItemDto createCartItemDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItemToUserCart(user, createCartItemDto));
    }

    @Operation(summary = "Update an Item's quantity from current User's cart")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204", description = "Successful Operation with no content", content = @Content(
                            schema = @Schema(hidden = true)
                    )),
                    @ApiResponse(
                            responseCode = "400", description = "There was a problem with the request. One or more parameters failed some validations", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "404", description = "A certain resource has not been found", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "401", description = "The User is not authorized to perform this operation", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "403", description = "The User has insufficient permission to perform this operation", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "500", description = "There was an internal server error", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
            }
    )
    @PatchMapping(ApiRoutes.V1.Cart.UPDATE_ITEM_FROM_CART)
    public ResponseEntity<Void> updateCartItemQuantityFromUserCart(@AuthenticationPrincipal User user, @PathVariable Long productId,
                                                                   @RequestBody UpdateCartItemDto updateCartItemDto) {
        cartService.updateItemQuantityFromUserCart(user, productId, updateCartItemDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove an Item from current User's cart")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204", description = "Successful Operation with no content", content = @Content(
                            schema = @Schema(hidden = true)
                    )),
                    @ApiResponse(
                            responseCode = "401", description = "The User is not authorized to perform this operation", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "403", description = "The User has insufficient permission to perform this operation", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "500", description = "There was an internal server error", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
            }
    )
    @DeleteMapping(ApiRoutes.V1.Cart.REMOVE_ITEM_FROM_CART)
    public ResponseEntity<Void> removeItemFromCart(@AuthenticationPrincipal User user, @PathVariable Long productId) {
        cartService.removeItem(user, productId);
        return ResponseEntity.noContent().build();
    }
}
