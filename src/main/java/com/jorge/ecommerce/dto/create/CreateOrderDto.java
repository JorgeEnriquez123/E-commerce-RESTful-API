package com.jorge.ecommerce.dto.create;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderDto {
    @NotNull
    @DecimalMin("0.1")
    private BigDecimal total;
    @NotNull
    @Positive
    private Long userId;
    @NotNull
    @Positive
    private Long shippingAddressId;
}
