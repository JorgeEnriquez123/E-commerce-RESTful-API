package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.auth.LoginRequestDto;
import com.jorge.ecommerce.dto.auth.LoginResponseDto;
import com.jorge.ecommerce.dto.auth.RefreshTokenRequestDto;
import com.jorge.ecommerce.dto.create.CreateUserDto;
import com.jorge.ecommerce.handler.exception.FailedLoginException;
import com.jorge.ecommerce.handler.exception.FailedRefreshTokenException;
import com.jorge.ecommerce.jwt.JwtUtil;
import com.jorge.ecommerce.model.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager auth;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final EntityManager entityManager;

    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        log.info("Logging request: {}", loginRequestDto);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        try {
            log.info("Attempting to Log in");
            Authentication authResult = auth.authenticate(authenticationToken);
            var authenticatedUser = (User)authResult.getPrincipal();
            return LoginResponseDto.builder()
                    .token(jwtUtil.generateToken(authenticatedUser))
                    .refreshToken(jwtUtil.generateRefreshToken(authenticatedUser))
                    .build();
        }
        catch (AuthenticationException ex) {
            log.debug("Exception Details: ", ex);
            throw new FailedLoginException("Login failed, check credentials");
        }
    }

    public LoginResponseDto register(CreateUserDto createUserDto){
        log.info("Registering user: {}", createUserDto);
        userService.registerUser(createUserDto);
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .username(createUserDto.getUsername())
                .password(createUserDto.getPassword())
                .build();

        entityManager.clear();
        return login(loginRequestDto);
    }

    public LoginResponseDto refreshToken(RefreshTokenRequestDto requestDto) {
        log.info("Refreshing token: {}", requestDto);
        try{
            var refreshToken = requestDto.getRefreshToken();
            var username = jwtUtil.extractUsername(refreshToken);
            var userAuthenticated = userDetailsService.loadUserByUsername(username);

            if(jwtUtil.isTokenValid(refreshToken, userAuthenticated)) {
                return LoginResponseDto.builder()
                        .token(jwtUtil.generateToken(userAuthenticated))
                        .refreshToken(jwtUtil.generateRefreshToken(userAuthenticated))
                        .build();
            }
        }
        catch (Exception ex) {
            log.debug("Exception Details: ", ex);
            log.error("Refresh token could not be validated. {}", ex.getMessage());
            throw new FailedRefreshTokenException("Refresh token could not be validated");
        }
            return null;
    }
}
