package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.CreateUserDto;
import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<UserDto>> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<UserDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<UserDto> save(@Valid @RequestBody CreateUserDto createUserDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(createUserDto));
    }

    @PutMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserDto userDto){
        return ResponseEntity.ok().body(userService.update(id, userDto));
    }
}
