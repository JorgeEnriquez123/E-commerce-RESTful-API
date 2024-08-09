package com.jorge.ecommerce.dto.update;

import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty
    private String addressLine;
    @NotEmpty
    @Size(min = 2, max = 50)
    private String city;
    @NotEmpty
    @Size(min = 2, max = 50)
    private String district;
}
