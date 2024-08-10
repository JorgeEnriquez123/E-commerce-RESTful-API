package com.jorge.ecommerce.configuration.mapping;

import com.jorge.ecommerce.dto.CartItemDto;
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
    public void setupCartItemMappings(){
        modelMapper.createTypeMap(CartItem.class, CartItemDto.class)
                .addMapping(cartitem -> cartitem.getId().getProductId(), CartItemDto::setProductId)
                .addMapping(cartItem -> cartItem.getId().getCartId(), CartItemDto::setCartId)
                //Product's Category Mapping Config seems not to be working, so I'm setting it up here manually
                .<String>addMapping(cartItem -> cartItem.getProduct().getCategory().getName(),
                        (cartItemDto, value) -> cartItemDto.getProduct().setCategory(value));
    }
}
