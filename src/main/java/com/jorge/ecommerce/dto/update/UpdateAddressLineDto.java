package com.jorge.ecommerce.dto.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAddressLineDto {
    @Size(min = 2, max = 100)
    private String addressLine;
    @Size(min = 2, max = 50)
    private String city;
    @Size(min = 2, max = 50)
    private String district;
}
