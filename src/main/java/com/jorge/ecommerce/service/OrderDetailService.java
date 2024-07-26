package com.jorge.ecommerce.service;

import com.jorge.ecommerce.handler.exception.EntityNotFoundException;
import com.jorge.ecommerce.model.OrderDetail;
import com.jorge.ecommerce.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper;

    public OrderDetail findById(Long id) {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderDetail with id: " + id + " not found"));
    }

    public OrderDetail save(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }
}
