package com.jorge.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private BigDecimal total;
    private String status;
    private LocalDateTime dateTime;
    private AddressLineDto shippingAddress;
    private Set<OrderDetailDto> orderDetails;
}
