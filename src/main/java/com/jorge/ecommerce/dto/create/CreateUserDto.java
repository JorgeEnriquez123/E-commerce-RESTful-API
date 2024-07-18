package com.jorge.ecommerce.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDto {
    @Size(min = 2, max = 25)
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;
}
