package com.jorge.ecommerce.dto.create;

import com.jorge.ecommerce.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCartDto {
    @NotNull
    private Long userId;
}
