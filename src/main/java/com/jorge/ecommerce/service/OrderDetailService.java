package com.jorge.ecommerce.service;

import com.jorge.ecommerce.model.CartItem;
import com.jorge.ecommerce.model.Order;
import com.jorge.ecommerce.model.OrderDetail;
import com.jorge.ecommerce.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    protected OrderDetail save(OrderDetail orderDetail) {
        log.debug("Saving order detail: {} using repository", orderDetail);
        return orderDetailRepository.save(orderDetail);
    }

}
