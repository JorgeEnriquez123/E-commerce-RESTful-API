package com.jorge.ecommerce.service;

import com.jorge.ecommerce.model.CartItem;
import com.jorge.ecommerce.model.Order;
import com.jorge.ecommerce.model.OrderDetail;
import com.jorge.ecommerce.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    protected OrderDetail save(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    protected List<OrderDetail> saveAll(Set<OrderDetail> orderDetails){
        return orderDetailRepository.saveAll(orderDetails);
    }
}
