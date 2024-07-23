package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.create.CreateUserDto;
import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.handlers.exception.EntityNotFoundException;
import com.jorge.ecommerce.handlers.exception.ValueAlreadyExistsException;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found."));
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = findById(id);
        return convertToDto(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDto save(CreateUserDto createUserDto) {
        String username = createUserDto.getUsername();
        boolean usernameAlreadyExists = checkIfUsernameAlreadyExists(username);
        if(usernameAlreadyExists) {
            throw new ValueAlreadyExistsException("User with username: " + username + " already exists.");
        }
        User newUser = createUserFromDto(createUserDto);
        User savedUser = userRepository.save(newUser);
        return convertToDto(savedUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDto update(Long userId, CreateUserDto createUserDto) {
        User toUpdateUser = findById(userId);
        updateUserFromDto(toUpdateUser, createUserDto);

        User savedUpdatedUser = userRepository.save(toUpdateUser);
        return convertToDto(savedUpdatedUser);
    }

    private boolean checkIfUsernameAlreadyExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private User createUserFromDto(CreateUserDto createUserDto) {
        return modelMapper.map(createUserDto, User.class);
    }

    private void updateUserFromDto(User user, CreateUserDto createUserDto){
        modelMapper.map(createUserDto, user);
    }

    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
