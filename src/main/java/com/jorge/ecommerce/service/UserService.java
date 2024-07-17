package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.create.CreateUserDto;
import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.handlers.exception.EntityNotFoundException;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserDto.class))
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found."));
    }

    protected User findUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found."));
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDto save(CreateUserDto createUserDto) {
        User user = userRepository.save(
                modelMapper.map(createUserDto, User.class));
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDto update(Long id, CreateUserDto createUserDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found."));
        modelMapper.map(createUserDto, existingUser);
        userRepository.save(existingUser);
        return modelMapper.map(existingUser, UserDto.class);
    }
}
