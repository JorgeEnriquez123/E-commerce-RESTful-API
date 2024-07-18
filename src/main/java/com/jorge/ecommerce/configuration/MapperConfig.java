package com.jorge.ecommerce.configuration;

import com.jorge.ecommerce.dto.create.CreateProductDto;
import com.jorge.ecommerce.model.Product;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
