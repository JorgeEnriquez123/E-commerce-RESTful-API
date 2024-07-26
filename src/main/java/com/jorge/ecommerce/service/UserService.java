package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.dto.create.CreateUserDto;
import com.jorge.ecommerce.handler.exception.EntityNotFoundException;
import com.jorge.ecommerce.handler.exception.ValueAlreadyExistsException;
import com.jorge.ecommerce.model.Cart;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CartService cartService;
    private final AuthService authService;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, @Lazy CartService cartService, AuthService authService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.cartService = cartService;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<User> users = userRepository.findAll(pageable);
        return users.map(user -> modelMapper.map(user, UserDto.class));
    }

    @Transactional(readOnly = true)
    protected User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found."));
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username: " + username + " not found."));
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = findById(id);
        return convertToDto(user);
    }
    @Transactional(rollbackFor = Exception.class)
    public UserDto save(CreateUserDto createUserDto) {
        String username = createUserDto.getUsername();
        checkIfUsernameAlreadyExists(username);

        User newUser = createUserFromDto(createUserDto);
        encryptUserPassword(newUser);

        User savedUser = userRepository.save(newUser);

        // Creating a Cart for the User
        Cart cart = Cart.builder().user(savedUser).build();
        cartService.save(cart);

        return convertToDto(savedUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDto update(Long userId, CreateUserDto createUserDto) {
        User toUpdateUser = findById(userId);
        updateUserFromDto(toUpdateUser, createUserDto);
        encryptUserPassword(toUpdateUser);

        User savedUpdatedUser = userRepository.save(toUpdateUser);
        return convertToDto(savedUpdatedUser);
    }

    @Transactional(readOnly = true)
    protected void checkIfUsernameAlreadyExists(String username) {
        if (findByUsername(username) != null){
            throw new ValueAlreadyExistsException("User with username: " + username + " already exists.");
        }
    }

    private void encryptUserPassword(User user){
        user.setPassword(authService.encryptPassword(user.getPassword()));
    }

    private User createUserFromDto(CreateUserDto createUserDto) {
        return modelMapper.map(createUserDto, User.class);
    }

    private void updateUserFromDto(User user, CreateUserDto createUserDto){
        modelMapper.map(createUserDto, user);
    }

    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
