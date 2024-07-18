package com.jorge.ecommerce.dto.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCartItemDto {
    @NotNull
    private Long productId;
    @NotNull
    @Positive
    private Integer quantity;
    @NotNull
    private Long cartId;
}
