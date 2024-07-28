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

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final CartItemService cartItemService;
    private final ModelMapper modelMapper;

    public CartService(CartRepository cartRepository, @Lazy UserService userService,
                       @Lazy CartItemService cartItemService, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.userService = userService;
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
    public CartDto getCartById(Long id) {
        Cart cart = findById(id);
        return convertToDto(cart);
    }

    @Transactional(rollbackFor = Exception.class)
    public CartDto createCart(CreateCartDto createCartDto) {
        Cart newCart = createCartFromDto(createCartDto);
        Cart savedCart = save(newCart);
        return convertToDto(savedCart);
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
