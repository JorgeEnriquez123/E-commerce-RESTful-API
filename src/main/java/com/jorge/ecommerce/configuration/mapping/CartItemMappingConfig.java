package com.jorge.ecommerce.configuration.mapping;

import com.jorge.ecommerce.dto.CartItemDto;
import com.jorge.ecommerce.dto.create.CreateCartItemDto;
import com.jorge.ecommerce.model.CartItem;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CartItemMappingConfig {
    private final ModelMapper modelMapper;

    @PostConstruct
    public void setupCartItemMappings() {
        modelMapper.createTypeMap(CartItem.class, CartItemDto.class)
                .addMapping(CartItem::getProduct, CartItemDto::setProductDto);
        modelMapper.createTypeMap(CreateCartItemDto.class, CartItem.class)
                .addMappings(mapper -> {
                    mapper.skip(CartItem::setId);
                    mapper.skip(CartItem::setProduct);
                    mapper.skip(CartItem::setCart);
                });
    }
}
