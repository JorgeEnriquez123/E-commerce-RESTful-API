package com.jorge.ecommerce.integration;

import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init(){
        User user = User.builder()
                .username("Jorge")
                .password("JorgePass")
                .firstName("Jorge")
                .lastName("Enriquez")
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
    }

    @Test
    void testGetUserById(){
        ResponseEntity<UserDto> response =
                restTemplate.getForEntity("/user/" + 1L, UserDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }
}
