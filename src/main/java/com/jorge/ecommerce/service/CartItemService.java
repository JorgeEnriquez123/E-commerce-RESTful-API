package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.CartItemDto;
import com.jorge.ecommerce.handler.exception.ResourceNotFoundException;
import com.jorge.ecommerce.model.Cart;
import com.jorge.ecommerce.model.CartItem;
import com.jorge.ecommerce.model.Product;
import com.jorge.ecommerce.repository.CartItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CartItemService {
    private final ModelMapper modelMapper;

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    public CartItemService(CartItemRepository cartItemRepository,
                           ProductService productService, ModelMapper modelMapper) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    protected List<CartItem> findAllByCartId(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Transactional(readOnly = true)
    protected CartItem findById(CartItem.CartItemPk id) {
        log.debug("Finding cart item by id: {} using repository", id);
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem with id: " + id + " not found."));
    }

    @Transactional(rollbackFor = Exception.class)
    protected void deleteById(CartItem.CartItemPk id) {
        log.debug("Deleting cart item by id: {} using repository", id);
        cartItemRepository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    protected CartItem save(CartItem cartItem){
        log.debug("Saving cart item: {} using repository", cartItem);
        return cartItemRepository.save(cartItem);
    }

    // This avoids an extra SELECT query when saving an entity with a set ID, which is the case for a Composite Entity
    @Transactional(rollbackFor = Exception.class)
    protected void insertCartItem(CartItem cartItem){
        log.debug("Inserting cart item: {} using repository with custom query", cartItem);
        cartItemRepository.insertCartItem(cartItem);
    }

    @Transactional(rollbackFor = Exception.class)
    public CartItemDto saveCartItem(Cart cart, Long productId, Integer quantity) {
        Long cartId = cart.getId();

        CartItem.CartItemPk cartItemPk = CartItem.CartItemPk.builder()
                .productId(productId)
                .cartId(cartId)
                .build();

        try {
            log.info("Checking if Cart Item is already in Cart");
            CartItem cartItem = findById(cartItemPk);

            log.info("Cart Item is already in Cart. Updating cart item quantity");
            Integer currentQuantity = cartItem.getQuantity();

            cartItem.setQuantity(currentQuantity + quantity);
            save(cartItem);

            return convertToDto(cartItem);
        }
        catch (ResourceNotFoundException e){
            log.info("Cart Item is not in Cart");
            Product product = productService.findById(productId);

            CartItem newCartItem = CartItem.builder()
                    .id(cartItemPk)
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();

            insertCartItem(newCartItem);

            return convertToDto(newCartItem);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCartItemQuantity(Long cartId, Long productId, Integer quantity) {
        CartItem.CartItemPk cartItemPk = CartItem.CartItemPk.builder()
                .productId(productId)
                .cartId(cartId)
                .build();

        CartItem cartItem = findById(cartItemPk);

        cartItem.setQuantity(quantity);

        save(cartItem);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteCartItem(Long cartId, Long productId) {
        CartItem.CartItemPk cartItemPk = CartItem.CartItemPk.builder()
                .productId(productId)
                .cartId(cartId)
                .build();

        deleteById(cartItemPk);
    }

    protected CartItemDto convertToDto(CartItem cartItem) {
        log.debug("Mapping cart item: {}, to Dto", cartItem);
        return modelMapper.map(cartItem, CartItemDto.class);
    }
}
