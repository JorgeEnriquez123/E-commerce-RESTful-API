package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void testFindUserById(){
        Long userId = 1L;
        User user = User.builder()
                .id(1L)
                .username("Jorge123")
                .password("JorgePass")
                .firstName("Jorge")
                .lastName("Enriquez")
                .build();

        UserDto expectedUserDto = UserDto.builder()
                .id(1L)
                .username("Jorge123")
                .password("JorgePass")
                .firstName("Jorge")
                .lastName("Enriquez")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(expectedUserDto);

        UserDto returnedUserDto = userService.getUserById(1L);

        assertNotNull(returnedUserDto);
        assertEquals(expectedUserDto, returnedUserDto);
    }
}
