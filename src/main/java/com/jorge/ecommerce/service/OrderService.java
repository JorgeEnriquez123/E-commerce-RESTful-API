package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.CartDto;
import com.jorge.ecommerce.dto.OrderDto;
import com.jorge.ecommerce.dto.create.CreateOrderDto;
import com.jorge.ecommerce.handlers.exception.EntityNotFoundException;
import com.jorge.ecommerce.model.*;
import com.jorge.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    private final UserService userService;
    private final AddressLineService addressLineService;
    private final CartService cartService;
    private final OrderDetailService orderDetailService;
    private final CartItemService cartItemService;

    @Transactional(readOnly = true)
    public List<Order> findAll(){
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order findById(Long id){
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id: " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id){
        Order order = findById(id);
        return convertToDto(order);
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderDto save(CreateOrderDto createOrderDto){
        Order newOrder = createOrderFromDto(createOrderDto);
        Order savedOrder = orderRepository.save(newOrder);
        return convertToDto(savedOrder);
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderDto createOrder(CreateOrderDto createOrderDto){
        Long userId = createOrderDto.getUserId();
        Long shippingAddressId = createOrderDto.getShippingAddressId();

        User user = userService.findById(userId);
        AddressLine addressLine = addressLineService.findById(shippingAddressId);
        Cart cartFromUser = cartService.findByUserId(userId);
        BigDecimal total = calculateTotal(cartFromUser);

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(addressLine);
        order.setTotal(total);
        Order savedOrder = orderRepository.save(order);

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
                }
        );
        return convertToDto(savedOrder);
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

    private Order createOrderFromDto(CreateOrderDto createOrderDto) {
        User user = userService.findById(createOrderDto.getUserId());
        AddressLine addressLine = addressLineService.findById(createOrderDto.getUserId());

        Order order = modelMapper.map(createOrderDto, Order.class);
        order.setUser(user);
        order.setShippingAddress(addressLine);
        return order;
    }

    public OrderDto convertToDto(Order order){
        return modelMapper.map(order, OrderDto.class);
    }
}
