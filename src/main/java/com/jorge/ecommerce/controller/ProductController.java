package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.annotations.RoleAdmin;
import com.jorge.ecommerce.annotations.RoleAdminOrCustomer;
import com.jorge.ecommerce.dto.ProductDto;
import com.jorge.ecommerce.dto.create.CreateProductDto;
import com.jorge.ecommerce.dto.update.UpdateProductDto;
import com.jorge.ecommerce.service.ProductService;
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

    @RoleAdminOrCustomer
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAll(@RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer size,
                                                   @RequestParam(defaultValue = "asc") String sortOrder,
                                                   @RequestParam(defaultValue = "id") String sortBy){
        return ResponseEntity.ok(productService.findAll(page, size, sortOrder, sortBy));
    }

    @RoleAdminOrCustomer
    @GetMapping(ApiRoutes.V1.Product.GET_BY_ID)
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId){
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PostMapping
    public ResponseEntity<ProductDto> save(@Valid @RequestBody CreateProductDto createProductDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(createProductDto));
    }

    @PutMapping(ApiRoutes.V1.Product.PUT_UPDATE)
    public ResponseEntity<ProductDto> update(@PathVariable Long productId, @Valid @RequestBody UpdateProductDto updateProductDto){
        return ResponseEntity.ok(productService.updateProduct(productId, updateProductDto));
    }
}
