package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.annotations.RoleAdmin;
import com.jorge.ecommerce.annotations.RoleAdminOrCustomer;
import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.dto.auth.AddRoleToUserDto;
import com.jorge.ecommerce.dto.update.UpdateUserDto;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(
        name = "bearerAuth"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@RoleAdminOrCustomer
public class UserController {
    private final UserService userService;

    @PutMapping
    public ResponseEntity<UserDto> updateUserPersonalInfo(@AuthenticationPrincipal User user, @RequestBody UpdateUserDto updateUserDto){
        return ResponseEntity.ok().body(userService.updateUserPersonalInfo(user, updateUserDto));
    }

    // -----------

    @RoleAdmin
    @GetMapping
    public ResponseEntity<Page<UserDto>> getAll(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer size,
                                                @RequestParam(defaultValue = "asc") String sortOrder,
                                                @RequestParam(defaultValue = "id") String sortBy){
        return ResponseEntity.ok(userService.findAll(page, size, sortOrder, sortBy));
    }

    @RoleAdmin
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @RoleAdmin
    @PostMapping("/{userId}/roles")
    public ResponseEntity<Void> addRoleToUser(@PathVariable Long userId, @RequestBody AddRoleToUserDto addRoleToUserDto){
        userService.addRoleToUser(userId, addRoleToUserDto);
        return ResponseEntity.noContent().build();
    }

    @RoleAdmin
    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<UserDto> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId){
        userService.deleteRoleFromUser(userId, roleId);
        return ResponseEntity.noContent().build();
    }
}
