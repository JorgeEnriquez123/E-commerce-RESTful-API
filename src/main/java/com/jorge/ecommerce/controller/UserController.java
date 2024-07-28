package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.dto.create.CreateUserDto;
import com.jorge.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> findAll(@RequestParam Integer page, @RequestParam Integer size){
        return ResponseEntity.ok(userService.findAll(page, size));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findById(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> update(@PathVariable Long userId, @RequestBody CreateUserDto createUserDto){
        return ResponseEntity.ok().body(userService.updateUser(userId, createUserDto));
    }
}
