package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.CartDto;
import com.jorge.ecommerce.dto.create.CreateCartDto;
import com.jorge.ecommerce.handlers.exception.EntityNotFoundException;
import com.jorge.ecommerce.model.Cart;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public CartService(CartRepository cartRepository, @Lazy UserService userService, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<CartDto> findAll(){
        return cartRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    protected Cart findById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart with id: " + id + " not found"));
    }

    @Transactional(readOnly = true)
    protected Cart findByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart of User with id: " + userId + " not found"));
    }

    @Transactional(readOnly = true)
    public CartDto getCartById(Long id) {
        Cart cart = findById(id);
        return convertToDto(cart);
    }

    @Transactional
    public Cart save(Cart cart){
        return cartRepository.save(cart);
    }

    @Transactional(rollbackFor = Exception.class)
    public CartDto createCart(CreateCartDto createCartDto) {
        Cart newCart = createCartFromDto(createCartDto);
        Cart savedCart = save(newCart);
        return convertToDto(savedCart);
    }

    private Cart createCartFromDto(CreateCartDto createCartDto) {
        User user = userService.findById(createCartDto.getUserId());
        return Cart.builder()
                .user(user)
                .build();
    }

    private CartDto convertToDto(Cart cart) {
        return modelMapper.map(cart, CartDto.class);
    }
}
