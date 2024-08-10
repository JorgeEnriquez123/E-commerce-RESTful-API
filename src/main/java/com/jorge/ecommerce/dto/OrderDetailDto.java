package com.jorge.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDto {
    @JsonIgnore
    private Long orderId;
    @JsonIgnore
    private Long productId;
    private ProductDto product;
    private Integer quantity;
    private BigDecimal price;
}
