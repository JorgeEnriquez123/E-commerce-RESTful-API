package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.CartDto;
import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.dto.create.CreateCartDto;
import com.jorge.ecommerce.dto.create.CreateCategoryDto;
import com.jorge.ecommerce.handlers.exception.EntityNotFoundException;
import com.jorge.ecommerce.model.Cart;
import com.jorge.ecommerce.model.Category;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public List<CartDto> findAll(){
        return cartRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public CartDto findById(Long id) {
        return cartRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("Cart with id: " + id + " not found"));
    }

    protected Cart findCartEntityById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart with id: " + id + " not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    public CartDto save(CreateCartDto createCartDto) {
        Cart newCart = createCartFromDto(createCartDto);
        Cart savedCart = cartRepository.save(newCart);
        return convertToDto(savedCart);
    }

    public Cart createCartFromDto(CreateCartDto createCartDto) {
        User user = userService.findUserEntityById(createCartDto.getUserId());
        return Cart.builder()
                .user(user)
                .build();
    }

    public CartDto convertToDto(Cart cart) {
        return modelMapper.map(cart, CartDto.class);
    }
}
