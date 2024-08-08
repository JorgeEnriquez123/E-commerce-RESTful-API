package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.auth.LoginRequestDto;
import com.jorge.ecommerce.dto.auth.LoginResponseDto;
import com.jorge.ecommerce.dto.auth.RefreshTokenRequestDto;
import com.jorge.ecommerce.dto.create.CreateUserDto;
import com.jorge.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> register(@Valid @RequestBody CreateUserDto createUserDto){
        return ResponseEntity.ok(authService.register(createUserDto));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto requestDto){
        return ResponseEntity.ok(authService.refreshToken(requestDto));
    }
}
