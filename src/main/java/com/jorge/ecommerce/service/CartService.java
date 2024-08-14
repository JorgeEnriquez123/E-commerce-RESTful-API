package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.CartDto;
import com.jorge.ecommerce.dto.CartItemDto;
import com.jorge.ecommerce.dto.create.CreateCartItemDto;
import com.jorge.ecommerce.dto.update.UpdateCartItemDto;
import com.jorge.ecommerce.model.Cart;
import com.jorge.ecommerce.model.CartItem;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartService {
    private final ModelMapper modelMapper;

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;

    public CartService(CartRepository cartRepository, @Lazy CartItemService cartItemService,
                       ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.modelMapper = modelMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    protected Cart save(Cart cart){
        log.debug("Saving cart: {} using repository", cart);
        return cartRepository.save(cart);
    }

    @Transactional(readOnly = true)
    public List<CartItemDto> getItemsFromUser(User user) {
        log.debug("Getting cart items from user: {}", user);
        Long cartId = user.getCart().getId();
        List<CartItem> cartItems = cartItemService.findAllByCartId(cartId);
        return cartItems.stream()
                .map(cartItemService::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public CartItemDto addItemToUserCart(User user, CreateCartItemDto createCartItemDto) {
        log.debug("Adding Item to Cart from user: {}, createCartItemDto: {}", user, createCartItemDto);
        Cart cart = user.getCart();

        Long productId = createCartItemDto.getProductId();
        Integer quantity = createCartItemDto.getQuantity();

        return cartItemService.saveCartItem(cart, productId, quantity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateItemQuantityFromUserCart(User user, Long productId, UpdateCartItemDto updateCartItemDto) {
        log.debug("Updating Item Quantity from User: {}, updateCartItemDto: {}", user, updateCartItemDto);
        Long cartId = user.getCart().getId();

        Integer quantity = updateCartItemDto.getQuantity();

        cartItemService.updateCartItemQuantity(cartId, productId, quantity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeItem(User user, Long productId){
        log.debug("Removing Item from Cart from user: {}, productId: {}", user, productId);
        Long cartId = user.getCart().getId();
        cartItemService.deleteCartItem(cartId, productId);
    }

    private CartDto convertToDto(Cart cart) {
        log.debug("Mapping cart: {} to Dto", cart);
        return modelMapper.map(cart, CartDto.class);
    }
}
