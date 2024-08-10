package com.jorge.ecommerce.configuration.mapping;

import com.jorge.ecommerce.dto.OrderDetailDto;
import com.jorge.ecommerce.model.Order;
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
    public void setupOrderDetailMappings() {
        modelMapper.createTypeMap(OrderDetail.class, OrderDetailDto.class)
                .addMapping(orderDetail -> orderDetail.getId().getProductId(), OrderDetailDto::setProductId)
                .addMapping(orderDetail -> orderDetail.getId().getOrderId(), OrderDetailDto::setOrderId)
                //Product's Category Mapping Config seems not to be working, so I'm setting it up here manually
                .<String>addMapping(orderDetail -> orderDetail.getProduct().getCategory().getName(),
                        (cartItemDto, value) -> cartItemDto.getProduct().setCategory(value));
    }
}
