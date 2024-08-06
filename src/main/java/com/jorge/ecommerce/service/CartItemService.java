package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.CartItemDto;
import com.jorge.ecommerce.dto.create.CreateCartItemDto;
import com.jorge.ecommerce.handler.exception.ResourceNotFoundException;
import com.jorge.ecommerce.model.Cart;
import com.jorge.ecommerce.model.CartItem;
import com.jorge.ecommerce.model.Product;
import com.jorge.ecommerce.repository.CartItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    public CartItemService(CartItemRepository cartItemRepository,
                           ProductService productService, ModelMapper modelMapper) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    protected CartItem findByIdAndCartId(Long id, Long cartId) {
        return cartItemRepository.findByIdAndCartId(id, cartId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem with id: " + id + " not found."));
    }

    protected void deleteByIdAndCartId(Long id, Long cartId) {
        cartItemRepository.deleteByIdAndCartId(id, cartId);
    }

    @Transactional(rollbackFor = Exception.class)
    protected CartItem save(CartItem cartItem){
        return cartItemRepository.save(cartItem);
    }


    @Transactional(rollbackFor = Exception.class)
    public CartItemDto saveCartItem(Cart cart, CreateCartItemDto createCartItemDto) {
        Product product = productService.findById(createCartItemDto.getProductId());

        CartItem newCartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(createCartItemDto.getQuantity())
                .build();

        /*First Approach directly in CartService
        cart.getCartItems().add(newCartItem);
        cartRepository.save(cart);*/

        CartItem savedCartItem = save(newCartItem);
        return convertToDto(savedCartItem);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCartItemQuantity(Cart cart, Long cartItemId, Integer quantity) {
        CartItem cartItem = findByIdAndCartId(cart.getId(), cartItemId);

        cartItem.setQuantity(quantity);

        save(cartItem);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteCartItem(Cart cart, Long cartItemId) {
        deleteByIdAndCartId(cartItemId, cart.getId());
    }

    protected CartItemDto convertToDto(CartItem cartItem) {
        return modelMapper.map(cartItem, CartItemDto.class);
    }
}
