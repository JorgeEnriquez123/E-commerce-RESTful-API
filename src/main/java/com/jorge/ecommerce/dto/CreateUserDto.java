package com.jorge.ecommerce.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateUserDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
}
