package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.CartItemDto;
import com.jorge.ecommerce.dto.create.CreateCartItemDto;
import com.jorge.ecommerce.handlers.exception.EntityNotFoundException;
import com.jorge.ecommerce.model.Cart;
import com.jorge.ecommerce.model.CartItem;
import com.jorge.ecommerce.model.Product;
import com.jorge.ecommerce.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    protected CartItem findById(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CartItem with id: " + id + " not found."));
    }
    public List<CartItemDto> findByCartId(Long cartId) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId)
                .orElse(Collections.emptyList());
        if(cartItems.isEmpty())
            throw new EntityNotFoundException("No Cart items found for Cart with id: " + cartId);

        return cartItems.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public CartItemDto saveNewItemToCart(CreateCartItemDto createDto) {
        CartItem newCartItem = createCartItemFromDto(createDto);
        CartItem savedCartItem = cartItemRepository.save(newCartItem);
        return convertToDto(savedCartItem);
    }

    @Transactional(rollbackFor = Exception.class)
    public CartItemDto updateItemQuantityByCartId(Long cartItemId, Integer quantity) {
        CartItem toUpdateCartItem = findById(cartItemId);
        toUpdateCartItem.setQuantity(quantity);
        CartItem savedUpdatedCartItem = cartItemRepository.save(toUpdateCartItem);
        return convertToDto(savedUpdatedCartItem);
    }

    private CartItem createCartItemFromDto(CreateCartItemDto createCartItemDto) {
        Cart cart = cartService.findCartEntityById(createCartItemDto.getCartId());
        Product product = productService.findProductEntityById(createCartItemDto.getProductId());

        CartItem newCartItem = modelMapper.map(createCartItemDto, CartItem.class);
        newCartItem.setCart(cart);
        newCartItem.setProduct(product);
        return newCartItem;
    }

    private CartItemDto convertToDto(CartItem cartItem) {
        return modelMapper.map(cartItem, CartItemDto.class);
    }
}
