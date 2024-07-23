package com.jorge.ecommerce.configuration.mapping;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OrderMappingConfig {
    private final ModelMapper modelMapper;

    @PostConstruct
    public void setupOrderMappings() {
    }
}
