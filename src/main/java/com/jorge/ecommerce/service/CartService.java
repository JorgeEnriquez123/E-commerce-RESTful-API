package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.CartDto;
import com.jorge.ecommerce.dto.CartItemDto;
import com.jorge.ecommerce.dto.create.CreateCartItemDto;
import com.jorge.ecommerce.dto.create.CreateCartDto;
import com.jorge.ecommerce.handler.exception.EntityNotFoundException;
import com.jorge.ecommerce.model.Cart;
import com.jorge.ecommerce.model.CartItem;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.CartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemService cartItemService;
    private final ModelMapper modelMapper;

    public CartService(CartRepository cartRepository, @Lazy CartItemService cartItemService,
                       ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.modelMapper = modelMapper;
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

    @Transactional
    protected Cart save(Cart cart){
        return cartRepository.save(cart);
    }

    @Transactional(readOnly = true)
    public List<CartItemDto> getItems(Long cartId) {
        List<CartItem> cartItems = cartItemService.findByCartId(cartId);
        return cartItems.stream()
                .map(cartItemService::convertToDto)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public CartItemDto addItem(Long cartId, CreateCartItemDto createCartItemDto) {
        CartItem newCartItem = cartItemService.createCartItem(cartId, createCartItemDto);
        CartItem savedCartItem = cartItemService.save(newCartItem);
        return cartItemService.convertToDto(savedCartItem);
    }

    @Transactional(rollbackFor = Exception.class)
    public CartItemDto updateItemQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemService.findById(cartItemId);
        cartItem.setQuantity(quantity);

        CartItem updatedCartItem = cartItemService.save(cartItem);
        return cartItemService.convertToDto(updatedCartItem);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeItem(Long cartItemId){
        cartItemService.deleteById(cartItemId);
    }

    private CartDto convertToDto(Cart cart) {
        return modelMapper.map(cart, CartDto.class);
    }
}
