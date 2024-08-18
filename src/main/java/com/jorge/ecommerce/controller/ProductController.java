package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.annotations.RoleAdmin;
import com.jorge.ecommerce.annotations.RoleAdminOrCustomer;
import com.jorge.ecommerce.dto.ProductDto;
import com.jorge.ecommerce.dto.create.CreateProductDto;
import com.jorge.ecommerce.dto.pagination.PaginatedProductResponse;
import com.jorge.ecommerce.dto.update.UpdateProductDto;
import com.jorge.ecommerce.handler.response.ErrorResponse;
import com.jorge.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(
        name = "bearerAuth"
)
@RestController
@RequestMapping(ApiRoutes.V1.Product.ROOT)
@RequiredArgsConstructor
@RoleAdmin
@Tag(name = "Products", description = "Products from the shop")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Get all Products with pagination")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Successful operation", content = @Content(
                            schema = @Schema(implementation = PaginatedProductResponse.class)
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
    @RoleAdminOrCustomer
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAll(@RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer size,
                                                   @RequestParam(defaultValue = "asc") String sortOrder,
                                                   @RequestParam(defaultValue = "id") String sortBy){
        return ResponseEntity.ok(productService.findAll(page, size, sortOrder, sortBy));
    }

    @Operation(summary = "Get Product by Id")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Successful operation", content = @Content(
                            schema = @Schema(implementation = ProductDto.class)
                    )),
                    @ApiResponse(
                            responseCode = "404", description = "A certain resource has not been found", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
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
    @RoleAdminOrCustomer
    @GetMapping(ApiRoutes.V1.Product.GET_BY_ID)
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId){
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @Operation(summary = "Save a new Product")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201", description = "Resource successfully created", content = @Content(
                            schema = @Schema(implementation = ProductDto.class)
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
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody CreateProductDto createProductDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(createProductDto));
    }

    @Operation(summary = "Update a Product's info")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Successful operation", content = @Content(
                            schema = @Schema(implementation = ProductDto.class)
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
    @PutMapping(ApiRoutes.V1.Product.PUT_UPDATE)
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId, @Valid @RequestBody UpdateProductDto updateProductDto){
        return ResponseEntity.ok(productService.updateProduct(productId, updateProductDto));
    }
}
