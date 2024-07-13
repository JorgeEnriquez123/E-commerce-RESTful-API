package com.jorge.ecommerce.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateAddressLineDto {
    private String addressLine;
    private String city;
    private String district;
}
