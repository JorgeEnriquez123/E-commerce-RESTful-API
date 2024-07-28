package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.AddressLineDto;
import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.dto.create.CreateUserDto;
import com.jorge.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAll(@RequestParam Integer page, @RequestParam Integer size){
        return ResponseEntity.ok(userService.findAll(page, size));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUserPersonalInfo(@PathVariable Long userId, @RequestBody CreateUserDto createUserDto){
        return ResponseEntity.ok().body(userService.updateUser(userId, createUserDto));
    }

    @GetMapping("/{userId}/addressLines")
    public ResponseEntity<List<AddressLineDto>> getAllAddressLines(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getAddressLines(userId));
    }

    @PutMapping("/{userId}/addressLines/{addressLineId}/set-default")
    public ResponseEntity<Void> setDefaultAddressLine(@PathVariable Long userId, @PathVariable Long addressLineId){
        userService.setDefaultAddressLine(userId, addressLineId);
        return ResponseEntity.noContent().build();
    }
}
