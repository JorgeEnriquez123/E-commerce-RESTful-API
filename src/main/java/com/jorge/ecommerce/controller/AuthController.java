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
@RequestMapping(ApiRoutes.V1.Auth.ROOT)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(ApiRoutes.V1.Auth.LOGIN)
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping(ApiRoutes.V1.Auth.REGISTER)
    public ResponseEntity<LoginResponseDto> register(@Valid @RequestBody CreateUserDto createUserDto){
        return ResponseEntity.ok(authService.register(createUserDto));
    }

    @PostMapping(ApiRoutes.V1.Auth.REFRESH_TOKEN)
    public ResponseEntity<LoginResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto requestDto){
        return ResponseEntity.ok(authService.refreshToken(requestDto));
    }
}
