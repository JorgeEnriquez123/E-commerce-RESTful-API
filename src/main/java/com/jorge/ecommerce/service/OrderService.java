package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.OrderDto;
import com.jorge.ecommerce.dto.create.CreateOrderDto;
import com.jorge.ecommerce.dto.update.UpdateOrderStatusDto;
import com.jorge.ecommerce.enums.OrderStatus;
import com.jorge.ecommerce.handler.exception.InvalidDataException;
import com.jorge.ecommerce.handler.exception.ResourceNotFoundException;
import com.jorge.ecommerce.model.*;
import com.jorge.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    protected Order findById(Long orderId) {
        log.debug("Finding orders by id: {} using repository", orderId);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id: " + orderId + " not found."));
    }

    @Transactional(rollbackFor = Exception.class)
    protected Order save(Order order){
        log.debug("Saving order: {} using repository", order);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderDto> getOrdersWithDetailsFromUser(Integer page, Integer pageSize, String sortOrder, String sortBy, User user) {
        log.debug("Finding all orders from current User");

        Sort sort = Sort.by(sortOrder.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        if(page <= 1) {
            page = 1;
        }
        if(pageSize <= 1) {
            pageSize = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

        Page<Order> orders = orderRepository.findByUserId(pageable, user.getId());
        return orders.map(this::convertToDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(User user, CreateOrderDto createOrderDto){
        log.debug("Creating order with shipping Address id: {} for user with username: {}", createOrderDto.getShippingAddressId(), user.getUsername());
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

                    orderDetailService.insertOrderDetail(orderDetail);

                    cartItemService.deleteById(cartItem.getId());

                    productService.reduceStock(cartItem.getProduct().getId(), cartItem.getQuantity());
                }
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void updatedOrderStatus(Long orderId, UpdateOrderStatusDto updateOrderStatusDto){
        String status = updateOrderStatusDto.getStatus();
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            Order order = findById(orderId);

            order.setStatus(orderStatus);
            orderRepository.save(order);
        }
        catch (IllegalArgumentException e){
            log.error("Invalid order status: {}", status);
            throw new InvalidDataException("Invalid order status: " + status);
        }
    }

    private BigDecimal calculateItemTotal(CartItem cartItem) {
        log.debug("Calculating total price for cart item: {}", cartItem);
        return cartItem.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    }

    private BigDecimal calculateTotal(List<CartItem> cartItems) {
        log.debug("Calculating total of cartItems");
        return cartItems.stream()
                .map(this::calculateItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderDto convertToDto(Order order){
        log.debug("Mapping order: {} to Dto", order);
        return modelMapper.map(order, OrderDto.class);
    }
}
