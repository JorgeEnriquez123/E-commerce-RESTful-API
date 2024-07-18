package com.jorge.ecommerce.configuration;

import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.model.User;
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
