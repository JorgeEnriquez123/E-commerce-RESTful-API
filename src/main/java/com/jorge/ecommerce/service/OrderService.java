package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.OrderDto;
import com.jorge.ecommerce.dto.create.CreateOrderDto;
import com.jorge.ecommerce.model.*;
import com.jorge.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final ModelMapper modelMapper;

    private final OrderRepository orderRepository;
    private final AddressLineService addressLineService;
    private final OrderDetailService orderDetailService;
    private final CartItemService cartItemService;
    private final ProductService productService;

    @Transactional(readOnly = true)
    protected List<Order> findOrdersWithDetailsByUserId(Long userId){
        log.debug("Find orders with details by user id: {} using repository", userId);
        return orderRepository.findOrdersWithDetailsByUserId(userId)
                .orElse(Collections.emptyList());
    }

    @Transactional(rollbackFor = Exception.class)
    protected Order save(Order order){
        log.debug("Save order: {} using repository", order);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersWithDetailsFromUser(User user) {
        log.debug("Get orders with details from user: {}", user);
        return findOrdersWithDetailsByUserId(user.getId())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(User user, CreateOrderDto createOrderDto){
        log.debug("Create order: {} from user: {}", createOrderDto, user);
        Long shippingAddressId = createOrderDto.getShippingAddressId();

        AddressLine addressLine = addressLineService.findById(shippingAddressId);

        Long cartId = user.getCart().getId();
        List<CartItem> cartItems = cartItemService.findAllByCartId(cartId);

        BigDecimal total = calculateTotal(cartItems);

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(addressLine);
        order.setTotal(total);

        Order savedOrder = save(order);

        log.debug("Creating order details based on each product");
        cartItems.forEach(
                cartItem -> {
                    Product cartItemProduct = cartItem.getProduct();
                    Integer cartItemQuantity = cartItem.getQuantity();

                    OrderDetail.OrderDetailPk orderDetailPk = OrderDetail.OrderDetailPk.builder()
                            .productId(cartItemProduct.getId())
                            .orderId(savedOrder.getId())
                            .build();

                    OrderDetail orderDetail = OrderDetail.builder()
                            .id(orderDetailPk)
                            .order(savedOrder)
                            .product(cartItemProduct)
                            .quantity(cartItemQuantity)
                            .price(cartItemProduct.getPrice())
                            .build();

                    orderDetailService.save(orderDetail);

                    cartItemService.deleteById(cartItem.getId());

                    productService.reduceStock(cartItem.getProduct().getId(), cartItem.getQuantity());
                }
        );
    }

    private BigDecimal calculateItemTotal(CartItem cartItem) {
        log.debug("Calculate total price for cart item: {}", cartItem);
        return cartItem.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    }

    private BigDecimal calculateTotal(List<CartItem> cartItems) {
        log.debug("Calculate total of cartItems");
        return cartItems.stream()
                .map(this::calculateItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderDto convertToDto(Order order){
        log.debug("Convert order: {} to Dto", order);
        return modelMapper.map(order, OrderDto.class);
    }
}
