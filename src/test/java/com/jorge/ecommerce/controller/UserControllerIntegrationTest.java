package com.jorge.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorge.ecommerce.dto.auth.AddRoleToUserDto;
import com.jorge.ecommerce.dto.update.UpdateUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserDetailsService userDetailsService;

    //Needed for endpoints with @AuthenticationPrincipal parameters
    @BeforeEach
    void setUpSecurityContext(){
        var user = userDetailsService.loadUserByUsername("Test111");
        System.out.println(user);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
                )
        );
    }

    // Test updateUserPersonalInfo for a logged-in user
    @Test
    //@WithMockUser(username = "Test123", roles = {"CUSTOMER"})
    public void testUpdateUserPersonalInfo() throws Exception {
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .username("Test123")
                .password("newPassword")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isOk());
    }

    // Test getAll for admin
    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(status().isOk());
    }

    // Test getUserById for admin
    /*@Test
    public void testGetUserById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/1"))
                .andExpect(status().isOk());
    }*/

    // Test addRoleToUser for admin
    @Test
    public void testAddRoleToUser() throws Exception {
        AddRoleToUserDto addRoleToUserDto = new AddRoleToUserDto();
        addRoleToUserDto.setRole("CUSTOMER");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addRoleToUserDto)))
                .andExpect(status().isOk());
    }
}