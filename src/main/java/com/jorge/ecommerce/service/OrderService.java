package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.OrderDto;
import com.jorge.ecommerce.dto.create.CreateOrderDto;
import com.jorge.ecommerce.handler.exception.ResourceNotFoundException;
import com.jorge.ecommerce.model.*;
import com.jorge.ecommerce.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    private final AddressLineService addressLineService;
    private final CartService cartService;
    private final OrderDetailService orderDetailService;
    private final CartItemService cartItemService;
    private final ProductService productService;

    @Transactional(readOnly = true)
    protected List<Order> findOrdersWithDetailsById(Long userId){
        return orderRepository.findOrdersWithDetailsByUserId(userId)
                .orElse(Collections.emptyList());
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersWithDetailsFromUser(User user) {
        final List<Order> orders = findOrdersWithDetailsById(user.getId());
        return orders
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    protected Order save(Order order){
        return orderRepository.save(order);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(User user, CreateOrderDto createOrderDto){
        Long shippingAddressId = createOrderDto.getShippingAddressId();

        AddressLine addressLine = addressLineService.findById(shippingAddressId);

        Cart cartFromUser = cartService.findCartWithItemsByUserId(user.getId());

        BigDecimal total = calculateTotal(cartFromUser);

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(addressLine);
        order.setTotal(total);

        Order savedOrder = save(order);

        Set<CartItem> cartItems = cartFromUser.getCartItems();
        cartItems.forEach(
                cartItem -> {
                    OrderDetail orderDetail = OrderDetail.builder()
                            .order(savedOrder)
                            .product(cartItem.getProduct())
                            .quantity(cartItem.getQuantity())
                            .price(cartItem.getProduct().getPrice())
                            .build();

                    orderDetailService.save(orderDetail);

                    cartItemService.deleteById(cartItem.getId());

                    productService.reduceStock(cartItem.getProduct().getId(), cartItem.getQuantity());
                }
        );
    }

    private BigDecimal calculateItemTotal(CartItem cartItem) {
        return cartItem.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    }

    private BigDecimal calculateTotal(Cart cartFromUser) {
        return cartFromUser.getCartItems().stream()
                .map(this::calculateItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderDto convertToDto(Order order){
        return modelMapper.map(order, OrderDto.class);
    }
}
