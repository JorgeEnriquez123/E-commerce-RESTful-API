package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testGetUserById() throws Exception {
        Long userId = 1L;

        UserDto expectedUserDto = UserDto.builder()
                .id(1L)
                .username("Jorge123")
                .password("JorgePass")
                .firstName("Jorge")
                .lastName("Enriquez")
                .build();

        when(userService.findById(userId)).thenReturn(expectedUserDto);

        mockMvc.perform(get("/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("Jorge123"));
    }
}
