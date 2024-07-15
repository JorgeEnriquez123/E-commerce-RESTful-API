package com.jorge.ecommerce.dto.create;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAddressLineDto {
    @Size(min = 2, max = 100)
    private String addressLine;
    @Size(min = 2, max = 50)
    private String city;
    @Size(min = 2, max = 50)
    private String district;
}
