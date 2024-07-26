package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.LoginRequestDto;
import com.jorge.ecommerce.dto.LoginResponseDto;
import com.jorge.ecommerce.handler.exception.FailedLoginException;
import com.jorge.ecommerce.jwt.JwtUtil;
import com.jorge.ecommerce.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager auth;
    private final PasswordEncoder passwordEncoder;

    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        try {
            Authentication authResult = auth.authenticate(authenticationToken);
            var authenticatedUser = (User)authResult.getPrincipal();
            return LoginResponseDto.builder()
                    .token(jwtUtil.generateToken(authenticatedUser))
                    .build();
        }
        catch (AuthenticationException e) {
            throw new FailedLoginException("Login failed, check credentials");
        }
    }
}
