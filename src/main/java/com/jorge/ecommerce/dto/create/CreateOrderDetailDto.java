package com.jorge.ecommerce.dto.create;

import com.jorge.ecommerce.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderDetailDto {
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}
