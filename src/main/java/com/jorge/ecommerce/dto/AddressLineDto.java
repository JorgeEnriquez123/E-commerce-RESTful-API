package com.jorge.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressLineDto {
    private Long id;
    private String addressLine;
    private String city;
    private String district;
    private Boolean isDefault;
}
