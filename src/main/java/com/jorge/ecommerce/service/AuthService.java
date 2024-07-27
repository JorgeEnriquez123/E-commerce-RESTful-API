package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.LoginRequestDto;
import com.jorge.ecommerce.dto.LoginResponseDto;
import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.dto.create.CreateUserDto;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager auth;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Transactional(rollbackFor = Exception.class)
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

    @Transactional(rollbackFor = Exception.class)
    public LoginResponseDto register(CreateUserDto createUserDto){
        userService.registerUser(createUserDto);
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .username(createUserDto.getUsername())
                .password(createUserDto.getPassword())
                .build();

        return login(loginRequestDto);
    }
}
