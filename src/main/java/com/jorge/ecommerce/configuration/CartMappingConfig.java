package com.jorge.ecommerce.configuration;

import com.jorge.ecommerce.dto.CartDto;
import com.jorge.ecommerce.model.Cart;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CartMappingConfig {
    private final ModelMapper modelMapper;

    @PostConstruct
    public void setupCartMappings(){
        modelMapper.createTypeMap(Cart.class, CartDto.class)
                .addMapping(Cart::getUser, CartDto::setUserDto);
    }
}
