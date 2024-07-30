package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.AddressLineDto;
import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.dto.create.CreateAddressLineDto;
import com.jorge.ecommerce.dto.create.CreateUserDto;
import com.jorge.ecommerce.handler.exception.ResourceNotFoundException;
import com.jorge.ecommerce.handler.exception.ValueAlreadyExistsException;
import com.jorge.ecommerce.model.Cart;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CartService cartService;
    private final AuthService authService;
    private final AddressLineService addressLineService;

    public UserService(UserRepository userRepository, ModelMapper modelMapper,
                       @Lazy CartService cartService, @Lazy AuthService authService, @Lazy AddressLineService addressLineService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.cartService = cartService;
        this.authService = authService;
        this.addressLineService = addressLineService;
    }

    @Transactional(readOnly = true)
    protected User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found."));
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username: " + username + " not found."));
    }

    @Transactional(rollbackFor = Exception.class)
    protected User save(User user) {
        checkIfUsernameAlreadyExists(user.getUsername());
        encryptUserPassword(user);

        User savedUser = userRepository.save(user);

        // Creating a Cart for the User
        Cart cart = Cart.builder().user(savedUser).build();
        cartService.save(cart);

        return user;
    }

    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy) {
        Sort sort = Sort.by(sortOrder.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        if(pageNumber <= 1) {
            pageNumber = 1;
        }
        if(pageSize <= 1) {
            pageSize = 1;
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<User> users = userRepository.findAll(pageable);
        return users.map(user -> modelMapper.map(user, UserDto.class));
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = findById(id);
        return convertToDto(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDto registerUser(CreateUserDto createUserDto) {
        User newUser = createUserFromDto(createUserDto);
        User savedUser = save(newUser);
        return convertToDto(savedUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDto updateUser(Long userId, CreateUserDto createUserDto) {
        User userToUpdate = findById(userId);
        updateUserFromDto(createUserDto, userToUpdate);
        encryptUserPassword(userToUpdate);

        User savedUpdatedUser = userRepository.save(userToUpdate);
        return convertToDto(savedUpdatedUser);
    }

    public AddressLineDto addAddressLine(Long userId, CreateAddressLineDto createAddressLineDto){
        return addressLineService.saveAddressLine(userId, createAddressLineDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<AddressLineDto> getAddressLines(Long userId) {
        return addressLineService.getByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public AddressLineDto updateAddressLine(Long addressLineId, CreateAddressLineDto createAddressLineDto) {
        return addressLineService.updateAddressLineById(addressLineId, createAddressLineDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void setDefaultAddressLine(Long userId, Long addressLineId) {
        addressLineService.setDefaultAddressLine(userId, addressLineId);
    }

    @Transactional(readOnly = true)
    protected void checkIfUsernameAlreadyExists(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            throw new ValueAlreadyExistsException("User with username: " + username + " already exists.");
        });
    }

    private void encryptUserPassword(User user){
        user.setPassword(authService.encryptPassword(user.getPassword()));
    }

    private User createUserFromDto(CreateUserDto createUserDto) {
        return modelMapper.map(createUserDto, User.class);
    }

    private void updateUserFromDto(CreateUserDto createUserDto, User userToUpdate){
        modelMapper.map(createUserDto, userToUpdate);
    }

    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
