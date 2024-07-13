package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.create.CreateUserDto;
import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.handlers.exception.EntityNotFoundException;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(user -> userDtos.add(
                modelMapper.map(user, UserDto.class)
        ));
        return userDtos;
    }

    public UserDto findById(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found."));
        return modelMapper.map(existingUser, UserDto.class);
    }

    public UserDto save(CreateUserDto createUserDto) {
        User user = modelMapper.map(createUserDto, User.class);
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    public UserDto update(Long id, CreateUserDto createUserDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found.")
        );
        modelMapper.map(createUserDto, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDto.class);
    }
}
