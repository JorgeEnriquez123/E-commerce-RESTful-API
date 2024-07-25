package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.OrderDetailDto;
import com.jorge.ecommerce.dto.create.CreateOrderDetailDto;
import com.jorge.ecommerce.handlers.exception.EntityNotFoundException;
import com.jorge.ecommerce.model.Order;
import com.jorge.ecommerce.model.OrderDetail;
import com.jorge.ecommerce.model.Product;
import com.jorge.ecommerce.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper;
    public List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    public OrderDetail findById(Long id) {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderDetail with id: " + id + " not found"));
    }

    public OrderDetailDto getOrderDetailById(Long id) {
        OrderDetail orderDetail = findById(id);
        return convertToDto(orderDetail);
    }

    public OrderDetail save(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    private OrderDetailDto convertToDto(OrderDetail orderDetail) {
        return modelMapper.map(orderDetail, OrderDetailDto.class);
    }

}
