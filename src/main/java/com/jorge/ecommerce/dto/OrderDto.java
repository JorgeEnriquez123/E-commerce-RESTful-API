package com.jorge.ecommerce.dto;

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
public class OrderDto {
    private Long id;
    private BigDecimal total;
    private String status;
    private LocalDateTime dateTime;
    private UserDto user;
    private AddressLineDto shippingAddress;

}
