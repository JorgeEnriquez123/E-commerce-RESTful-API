package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.AddressLineDto;
import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.dto.auth.AddRoleToUserDto;
import com.jorge.ecommerce.dto.create.CreateAddressLineDto;
import com.jorge.ecommerce.dto.create.CreateUserDto;
import com.jorge.ecommerce.dto.update.UpdateAddressLineDto;
import com.jorge.ecommerce.dto.update.UpdateUserDto;
import com.jorge.ecommerce.handler.exception.ResourceNotFoundException;
import com.jorge.ecommerce.handler.exception.UsernameAlreadyInUseException;
import com.jorge.ecommerce.model.Cart;
import com.jorge.ecommerce.model.Role;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final ModelMapper modelMapper;
    private final CacheManager cacheManager;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final CartService cartService;
    private final RoleService roleService;

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

        checkIfUsernameAlreadyExists(createUserDto.getUsername());

        Set<Role> roles = new HashSet<>();

        if(createUserDto.getRole() != null){
            String roleName = createUserDto.getRole();
            Role role = roleService.findByName(roleName);
            log.debug("Adding Role: {} to User", role);
            roles.add(role);
        }

        else {
            log.debug("Adding Default Role CUSTOMER to user");
            roles.add(roleService.findByName("CUSTOMER"));
        }

        User newUser = createUserFromDto(createUserDto);
        encryptUserPassword(newUser);

        newUser.setRoles(roles);

        User savedUser = save(newUser);

        // Creating a Cart for the User
        Cart cart = Cart.builder().user(savedUser).build();
        cartService.save(cart);

        return convertToDto(savedUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDto updateUserPersonalInfo(User user, UpdateUserDto updateUserDto) {
        log.debug("Updating user personal info: {}, new info: {}", user, updateUserDto);
        String oldUsername = user.getUsername();

        updateUserFromDto(user, updateUserDto);
        encryptUserPassword(user);

        User savedUpdatedUser = save(user);

        log.debug("Making Caching Up-to-date");
        if(oldUsername.equals(savedUpdatedUser.getUsername())) {
            updatedCachedUser(oldUsername, savedUpdatedUser);
        }
        else {
            evictCachedUser(oldUsername);
        }

        return convertToDto(savedUpdatedUser);
    }

    // ------------

    @Transactional(rollbackFor = Exception.class)
    public void addRoleToUser(Long userId, AddRoleToUserDto addRoleToUserDto) {
        log.debug("Adding Role: {} to User with id: {}", addRoleToUserDto.getRole(), userId);
        String roleName = addRoleToUserDto.getRole();
        Role role = roleService.findByName(roleName);

        User user = findById(userId);
        user.getRoles().add(role);

        User updatedUser = userRepository.save(user);

        updatedCachedUser(updatedUser.getUsername(), updatedUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleFromUser(Long userId, Long roleId) {
        log.debug("Deleting rol with id: {} from User with id: {}", roleId, userId);
        Role role = roleService.findById(roleId);

        User user = findById(userId);
        user.getRoles().remove(role);

        User updatedUser = userRepository.save(user);

        updatedCachedUser(user.getUsername(), updatedUser);
    }

    @Transactional(readOnly = true)
    protected void checkIfUsernameAlreadyExists(String username) {
        log.debug("Checking if username: {} already exists", username);
        long count = userRepository.countUsersByUsername(username);
        if(count > 0){
            throw new UsernameAlreadyInUseException("User with username: " + username + " already exists.");
        }
    }

    private void encryptUserPassword(User user){
        log.debug("Encrypting password for user: {}", user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    private User createUserFromDto(CreateUserDto createUserDto) {
        log.debug("Creating User from Dto: {}", createUserDto);
        return modelMapper.map(createUserDto, User.class);
    }

    private void updateUserFromDto(User user, UpdateUserDto updateUserDto){
        log.debug("Updating User from Dto: {}", updateUserDto);
        modelMapper.map(updateUserDto, user);
    }

    private void evictCachedUser(String username){
        log.debug("Deleting old cached user");
        Objects.requireNonNull(cacheManager.getCache("users")).evict(username);
        //User will have to log in again to get a new JWT with the updated username
    }

    private void updatedCachedUser(String username, User savedUpdatedUser){
        log.debug("Updating cached user");
        Objects.requireNonNull(cacheManager.getCache("users")).put(username, savedUpdatedUser);
        //Current JWT will still work
    }

    private UserDto convertToDto(User user) {
        log.debug("Mapping user: {} to Dto", user);
        return modelMapper.map(user, UserDto.class);
    }
}
