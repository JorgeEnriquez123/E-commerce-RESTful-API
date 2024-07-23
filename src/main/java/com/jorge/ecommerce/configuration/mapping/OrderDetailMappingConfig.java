package com.jorge.ecommerce.configuration.mapping;

import com.jorge.ecommerce.dto.OrderDetailDto;
import com.jorge.ecommerce.model.OrderDetail;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OrderDetailMappingConfig {
    private final ModelMapper modelMapper;

    @PostConstruct
    public void setupOrderDetailMappings(){
        modelMapper.createTypeMap(OrderDetail.class, OrderDetailDto.class)
                .addMapping(orderDetail -> orderDetail.getOrder().getId(), OrderDetailDto::setOrderId);
    }
}
