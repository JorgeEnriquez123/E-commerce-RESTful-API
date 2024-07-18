package com.jorge.ecommerce.configuration;

import com.jorge.ecommerce.dto.CartDto;
import com.jorge.ecommerce.dto.create.CreateAddressLineDto;
import com.jorge.ecommerce.model.AddressLine;
import com.jorge.ecommerce.model.Cart;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AddressLineMappingConfig {
    private final ModelMapper modelMapper;

    @PostConstruct
    public void setupAddressLineMappings(){
        modelMapper.createTypeMap(CreateAddressLineDto.class, AddressLine.class)
                .addMappings(mapper -> mapper.skip(AddressLine::setId));
    }
}
