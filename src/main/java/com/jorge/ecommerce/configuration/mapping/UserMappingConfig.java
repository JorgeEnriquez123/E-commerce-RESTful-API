package com.jorge.ecommerce.configuration.mapping;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UserMappingConfig {
    private final ModelMapper modelMapper;

    public void setupUserMappings(){

    }
}
