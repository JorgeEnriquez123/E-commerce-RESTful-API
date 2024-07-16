package com.jorge.ecommerce.dto.create;

import com.jorge.ecommerce.model.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductDto {
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal price;
    @PositiveOrZero
    @NotNull
    private Integer stockQuantity;
    @Positive
    private Long category;
}
