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
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CartService cartService;
    private final AuthService authService;
    private final AddressLineService addressLineService;
    private final CacheManager cacheManager;

    public UserService(UserRepository userRepository, ModelMapper modelMapper,
                       @Lazy CartService cartService, @Lazy AuthService authService,
                       @Lazy AddressLineService addressLineService, RedisCacheManager cacheManager) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.cartService = cartService;
        this.authService = authService;
        this.addressLineService = addressLineService;
        this.cacheManager = cacheManager;
    }

    @Transactional(readOnly = true)
    protected User findById(Long id) {
        log.debug("Finding user by id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found."));
    }

    @Cacheable(value = "users", key = "#username")
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username: " + username + " not found."));
    }

    @Transactional(rollbackFor = Exception.class)
    protected User save(User user) {
        log.debug("Saving user: {}", user);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Integer page, Integer pageSize, String sortOrder, String sortBy) {
        log.debug("Finding all users");

        Sort sort = Sort.by(sortOrder.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        if(page <= 1) {
            page = 1;
        }
        if(pageSize <= 1) {
            pageSize = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        log.debug("Getting user by id: {}", id);

        User user = findById(id);
        return convertToDto(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDto registerUser(CreateUserDto createUserDto) {
        log.debug("Registering user: {}", createUserDto);

        User newUser = createUserFromDto(createUserDto);
        checkIfUsernameAlreadyExists(newUser.getUsername());
        encryptUserPassword(newUser);

        User savedUser = save(newUser);

        // Creating a Cart for the User
        Cart cart = Cart.builder().user(savedUser).build();
        cartService.save(cart);

        return convertToDto(savedUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDto updateUserPersonalInfo(User user, CreateUserDto createUserDto) {
        log.debug("Updating user personal info: {}, new info: {}", user, createUserDto);

        User userToUpdate = findById(user.getId());
        String oldUsername = userToUpdate.getUsername();

        updateUserFromDto(userToUpdate, createUserDto);
        encryptUserPassword(userToUpdate);

        User savedUpdatedUser = save(userToUpdate);

        log.debug("Updating Chaching");

        if(!oldUsername.equals(savedUpdatedUser.getUsername())) {
            log.debug("Deleting old cached user");
            Objects.requireNonNull(cacheManager.getCache("users")).evict(oldUsername);
            //User will have to log in again to get a new JWT with the updated username
        }
        else {
            log.debug("Updating cached user");
            Objects.requireNonNull(cacheManager.getCache("users")).put(oldUsername, savedUpdatedUser);
            //Current JWT will still work
        }

        return convertToDto(savedUpdatedUser);
    }

    public AddressLineDto addAddressLine(User user, CreateAddressLineDto createAddressLineDto){
        log.debug("Adding address line: {}, for user: {}", createAddressLineDto, user);
        return addressLineService.saveAddressLine(user.getId(), createAddressLineDto);
    }

    public List<AddressLineDto> getAddressLines(User user) {
        log.debug("Getting address lines from user: {}", user);
        return addressLineService.getByUserId(user.getId());
    }

    public AddressLineDto updateAddressLine(User user, Long addressLineId, CreateAddressLineDto createAddressLineDto) {
        log.debug("Updating address line: {}, for user: {}", createAddressLineDto, user);
        return addressLineService.updateAddressLineById(user.getId(), addressLineId, createAddressLineDto);
    }

    public void setDefaultAddressLine(User user, Long addressLineId) {
        log.debug("Setting default address line by id: {}, for user: {}", addressLineId, user);
        addressLineService.setDefaultAddressLine(user.getId(), addressLineId);
    }

    @Transactional(readOnly = true)
    protected void checkIfUsernameAlreadyExists(String username) {
        log.debug("Checking if username: {} already exists", username);
        userRepository.findByUsername(username).ifPresent(user -> {
            throw new ValueAlreadyExistsException("User with username: " + username + " already exists.");
        });
    }

    private void encryptUserPassword(User user){
        log.debug("Encrypting password for user: {}", user);
        user.setPassword(authService.encryptPassword(user.getPassword()));
    }

    private User createUserFromDto(CreateUserDto createUserDto) {
        log.debug("Creating User from Dto: {}", createUserDto);
        return modelMapper.map(createUserDto, User.class);
    }

    private void updateUserFromDto(User user, CreateUserDto createUserDto){
        log.debug("Updating User from Dto: {}", createUserDto);
        modelMapper.map(createUserDto, user);
    }

    private UserDto convertToDto(User user) {
        log.debug("Mapping user: {} to Dto", user);
        return modelMapper.map(user, UserDto.class);
    }
}
