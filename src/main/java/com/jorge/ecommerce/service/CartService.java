package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.CartDto;
import com.jorge.ecommerce.dto.CartItemDto;
import com.jorge.ecommerce.dto.create.CreateCartItemDto;
import com.jorge.ecommerce.handler.exception.ResourceNotFoundException;
import com.jorge.ecommerce.model.Cart;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.CartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    protected Cart findByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart from User with id: " + userId + " not found"));
    }

    @Transactional(readOnly = true)
    protected Cart findCartWithItemsByUserId(Long userId) {
        return cartRepository.findCartWithItemsByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart of User with id: " + userId + " not found"));
    }

    @Transactional
    protected Cart save(Cart cart){
        return cartRepository.save(cart);
    }

    @Transactional(readOnly = true)
    public List<CartItemDto> getItemsFromUser(User user) {
        Cart cart = findCartWithItemsByUserId(user.getId());
        return cart.getCartItems().stream()
                .map(cartItemService::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CartItemDto addItemToUserCart(User user, CreateCartItemDto createCartItemDto) {
        Cart cart = findByUserId(user.getId());
        return cartItemService.saveCartItem(cart, createCartItemDto);
    }

    @Transactional
    public void updateItemQuantityFromUserCart(User user, Long cartItemId, Integer quantity) {
        Cart cart = findByUserId(user.getId());
        cartItemService.updateCartItemQuantity(cart, cartItemId, quantity);
    }

    @Transactional
    public void removeItem(User user, Long cartItemId){
        Cart cart = findByUserId(user.getId());
        cartItemService.deleteCartItem(cart, cartItemId);
    }

    private CartDto convertToDto(Cart cart) {
        return modelMapper.map(cart, CartDto.class);
    }
}
