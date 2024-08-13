package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.annotations.RoleAdmin;
import com.jorge.ecommerce.annotations.RoleAdminOrCustomer;
import com.jorge.ecommerce.dto.ProductDto;
import com.jorge.ecommerce.dto.create.CreateProductDto;
import com.jorge.ecommerce.dto.update.UpdateProductDto;
import com.jorge.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/products")
@RequiredArgsConstructor
@RoleAdmin
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
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId){
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PostMapping
    public ResponseEntity<ProductDto> save(@Valid @RequestBody CreateProductDto createProductDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(createProductDto));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> update(@PathVariable Long productId, @Valid @RequestBody UpdateProductDto updateProductDto){
        return ResponseEntity.ok(productService.updateProduct(productId, updateProductDto));
    }
}
