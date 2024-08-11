package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.AddressLineDto;
import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.dto.auth.AddRoleToUserDto;
import com.jorge.ecommerce.dto.create.CreateAddressLineDto;
import com.jorge.ecommerce.dto.create.CreateUserDto;
import com.jorge.ecommerce.dto.update.UpdateAddressLineDto;
import com.jorge.ecommerce.dto.update.UpdateUserDto;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<UserDto> updateUserPersonalInfo(@AuthenticationPrincipal User user, @RequestBody UpdateUserDto updateUserDto){
        return ResponseEntity.ok().body(userService.updateUserPersonalInfo(user, updateUserDto));
    }

    @GetMapping("/{userId}/addressLines")
    public ResponseEntity<List<AddressLineDto>> getAllAddressLines(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getAddressLines(user));
    }

    @PostMapping("/{userId}/addressLines")
    public ResponseEntity<AddressLineDto> addAddressLine(@AuthenticationPrincipal User user, @RequestBody CreateAddressLineDto createAddressLineDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addAddressLine(user, createAddressLineDto));
    }

    @PutMapping("/addressLines/{addressLineId}")
    public ResponseEntity<AddressLineDto> updateAddressLine(@AuthenticationPrincipal User user, @PathVariable Long addressLineId,
                                                            @RequestBody UpdateAddressLineDto updateAddressLineDto){
        return ResponseEntity.ok(userService.updateAddressLine(user, addressLineId, updateAddressLineDto));
    }

    @PutMapping("/{userId}/addressLines/{addressLineId}/set-default")
    public ResponseEntity<Void> setDefaultAddressLine(@AuthenticationPrincipal User user, @PathVariable Long addressLineId){
        userService.setDefaultAddressLine(user, addressLineId);
        return ResponseEntity.noContent().build();
    }

    // -----------

    @PutMapping("/{userId}/roles")
    public ResponseEntity<Void> addRoleToUser(@PathVariable Long userId, @RequestBody AddRoleToUserDto addRoleToUserDto){
        userService.addRoleToUser(userId, addRoleToUserDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<UserDto> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId){
        userService.deleteRoleFromUser(userId, roleId);
        return ResponseEntity.noContent().build();
    }
}
