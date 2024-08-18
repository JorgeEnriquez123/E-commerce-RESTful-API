package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.annotations.RoleAdmin;
import com.jorge.ecommerce.annotations.RoleAdminOrCustomer;
import com.jorge.ecommerce.dto.CategoryDto;
import com.jorge.ecommerce.dto.create.CreateCategoryDto;
import com.jorge.ecommerce.dto.update.UpdateCategoryDto;
import com.jorge.ecommerce.service.CategoryService;
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
@RequestMapping(ApiRoutes.V1.Category.ROOT)
@RequiredArgsConstructor
@RoleAdmin
@Tag(name = "Categories", description = "Category of Products")
public class CategoryController {
    private final CategoryService categoryService;

    @RoleAdminOrCustomer
    @GetMapping
    public ResponseEntity<Page<CategoryDto>> getAll(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size,
                                                    @RequestParam(defaultValue = "asc") String sortOrder,
                                                    @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(categoryService.findAll(page, size, sortOrder, sortBy));
    }

    @RoleAdminOrCustomer
    @GetMapping(ApiRoutes.V1.Category.GET_BY_ID)
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryDto categoryDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryDto));
    }

    @PutMapping(ApiRoutes.V1.Category.UPDATE)
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody UpdateCategoryDto updateCategoryDto) {
        return ResponseEntity.ok(categoryService.updateCategory(categoryId, updateCategoryDto));
    }
}
