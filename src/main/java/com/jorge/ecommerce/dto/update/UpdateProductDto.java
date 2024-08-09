package com.jorge.ecommerce.dto.update;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductDto {
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
    @NotNull
    private Long categoryId;
}
