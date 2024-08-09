package com.jorge.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    @JsonIgnore
    private Long cartId;
    @JsonIgnore
    private Long productId;
    private ProductDto product;
    private Integer quantity;
}
