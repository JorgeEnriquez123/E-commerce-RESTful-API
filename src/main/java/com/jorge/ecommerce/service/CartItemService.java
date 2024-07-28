package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.CartItemDto;
import com.jorge.ecommerce.dto.create.CreateCartItemDto;
import com.jorge.ecommerce.handler.exception.EntityNotFoundException;
import com.jorge.ecommerce.model.Cart;
import com.jorge.ecommerce.model.CartItem;
import com.jorge.ecommerce.model.Product;
import com.jorge.ecommerce.repository.CartItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    public CartItemService(CartItemRepository cartItemRepository, @Lazy CartService cartService,
                           ProductService productService, ModelMapper modelMapper) {
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    protected CartItem findById(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CartItem with id: " + id + " not found."));
    }

    @Transactional(readOnly = true)
    protected List<CartItem> findByCartId(Long cartId) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId)
                .orElse(Collections.emptyList());
        if(cartItems.isEmpty()) {
            throw new EntityNotFoundException("No items found from Cart with id: " + cartId);
        }
        return cartItems;
    }

    @Transactional(rollbackFor = Exception.class)
    protected CartItem save(CartItem cartItem){
        return cartItemRepository.save(cartItem);
    }

    @Transactional(rollbackFor = Exception.class)
    protected void deleteById(Long cartItemId){
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<CartItemDto> getCartItemsByCartId(Long cartId){
        List<CartItem> cartItems = findByCartId(cartId);
        return cartItems.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public CartItemDto saveCartItem(Long cartId, CreateCartItemDto createCartItemDto) {
        Cart cart = cartService.findById(cartId);
        Product product = productService.findById(createCartItemDto.getProductId());

        CartItem newCartItem = modelMapper.map(createCartItemDto, CartItem.class);
        newCartItem.setCart(cart);
        newCartItem.setProduct(product);

        CartItem savedCartItem = save(newCartItem);

        return convertToDto(savedCartItem);
    }

    @Transactional(rollbackFor = Exception.class)
    public CartItemDto updateCartItemQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = findById(cartItemId);
        cartItem.setQuantity(quantity);

        CartItem updatedCartItem = save(cartItem);
        return convertToDto(updatedCartItem);
    }

    protected CartItemDto convertToDto(CartItem cartItem) {
        return modelMapper.map(cartItem, CartItemDto.class);
    }
}
