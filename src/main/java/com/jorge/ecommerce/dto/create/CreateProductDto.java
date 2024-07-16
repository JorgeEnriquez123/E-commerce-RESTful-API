package com.jorge.ecommerce.dto.create;

import com.jorge.ecommerce.model.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductDto {
    @Size(min = 2, max = 50)
    private String name;
    @PositiveOrZero
    private Double price;
    @PositiveOrZero
    private Integer stockQuantity;
    @Positive
    private Long category;
}
