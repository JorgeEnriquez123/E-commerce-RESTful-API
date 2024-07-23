package com.jorge.ecommerce.dto;

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
    private Long id;
    private Long orderId;
    private ProductDto product;
    private Integer quantity;
    private BigDecimal price;
}
