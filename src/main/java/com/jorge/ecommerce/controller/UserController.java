package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.AddressLineDto;
import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.dto.create.CreateAddressLineDto;
import com.jorge.ecommerce.dto.create.CreateUserDto;
import com.jorge.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(
        name = "bearerAuth"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAll(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer size,
                                                @RequestParam(defaultValue = "asc") String sortOrder,
                                                @RequestParam(defaultValue = "id") String sortBy){
        return ResponseEntity.ok(userService.findAll(page, size, sortOrder, sortBy));
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

    @PostMapping("/{userId}/addressLines")
    public ResponseEntity<AddressLineDto> addAddressLine(@PathVariable Long userId, @RequestBody CreateAddressLineDto createAddressLineDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addAddressLine(userId, createAddressLineDto));
    }

    @PutMapping("/addressLines/{addressLineId}")
    public ResponseEntity<AddressLineDto> updateAddressLine(@PathVariable Long addressLineId, @RequestBody CreateAddressLineDto createAddressLineDto){
        return ResponseEntity.ok(userService.updateAddressLine(addressLineId, createAddressLineDto));
    }

    @PutMapping("/{userId}/addressLines/{addressLineId}/set-default")
    public ResponseEntity<Void> setDefaultAddressLine(@PathVariable Long userId, @PathVariable Long addressLineId){
        userService.setDefaultAddressLine(userId, addressLineId);
        return ResponseEntity.noContent().build();
    }
}
