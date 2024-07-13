package com.jorge.ecommerce.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateUserDto {
    @Size(min = 2, max = 25)
    private String username;
    @NotBlank
    private String password;
    @Size(min = 2, max = 50)
    private String firstName;
    @Size(min = 2, max = 50)
    private String lastName;
}
