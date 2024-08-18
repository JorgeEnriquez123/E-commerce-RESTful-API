package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.annotations.RoleAdmin;
import com.jorge.ecommerce.annotations.RoleAdminOrCustomer;
import com.jorge.ecommerce.dto.OrderDto;
import com.jorge.ecommerce.dto.ProductDto;
import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.dto.create.CreateOrderDto;
import com.jorge.ecommerce.dto.pagination.PaginatedOrderResponse;
import com.jorge.ecommerce.dto.update.UpdateOrderStatusDto;
import com.jorge.ecommerce.handler.response.ErrorResponse;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
@Tag(name = "Orders", description = "Orders from the User")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Get all Orders from current User with pagination")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Successful operation", content = @Content(
                            schema = @Schema(implementation = PaginatedOrderResponse.class)
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
    public ResponseEntity<Page<OrderDto>> getOrders(@AuthenticationPrincipal User user,
                                                    @RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size,
                                                    @RequestParam(defaultValue = "asc") String sortOrder,
                                                    @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(orderService.getOrdersWithDetailsFromUser(page, size, sortOrder, sortBy, user));
    }

    @Operation(summary = "Create a new Order from current User")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201", description = "Resource successfully created returning no content", content = @Content(
                            schema = @Schema(hidden = true)
                    )),
                    @ApiResponse(
                            responseCode = "400", description = "There was a problem with the request. One or more parameteres failed some validations", content = @Content(
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
    public ResponseEntity<Void> createOrder(@AuthenticationPrincipal User user, @RequestBody CreateOrderDto createOrderDto) {
        orderService.createOrder(user, createOrderDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update an Order's info")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204", description = "Operation successful with no content", content = @Content(
                            schema = @Schema(hidden = true)
                    )),
                    @ApiResponse(
                            responseCode = "400", description = "There was a problem with the request. One or more parameteres failed some validations", content = @Content(
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
    @RoleAdmin
    @PatchMapping(ApiRoutes.V1.Order.PUT_UPDATE_STATUS)
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long orderId, @RequestBody UpdateOrderStatusDto updateOrderStatusDto) {
        orderService.updatedOrderStatus(orderId, updateOrderStatusDto);
        return ResponseEntity.noContent().build();
    }
}
