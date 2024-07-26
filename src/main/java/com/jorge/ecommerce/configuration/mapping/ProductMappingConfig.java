package com.jorge.ecommerce.configuration.mapping;

import com.jorge.ecommerce.dto.ProductDto;
import com.jorge.ecommerce.model.Product;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ProductMappingConfig {
    private final ModelMapper modelMapper;

    @PostConstruct
    public void setupProductMappings(){
        modelMapper.createTypeMap(Product.class, ProductDto.class)
                .addMapping(product -> product.getCategory().getName(), ProductDto::setCategory);
    }
}
