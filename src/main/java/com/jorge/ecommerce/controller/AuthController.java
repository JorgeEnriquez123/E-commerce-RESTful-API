package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.auth.LoginRequestDto;
import com.jorge.ecommerce.dto.auth.LoginResponseDto;
import com.jorge.ecommerce.dto.auth.RefreshTokenRequestDto;
import com.jorge.ecommerce.dto.create.CreateUserDto;
import com.jorge.ecommerce.handler.response.ErrorResponse;
import com.jorge.ecommerce.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.V1.Auth.ROOT)
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication operations")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Perform an attempt to log in")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Successful operation", content = @Content(
                            schema = @Schema(implementation = LoginResponseDto.class)
                    )),
                    @ApiResponse(
                            responseCode = "401", description = "Login failed", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "500", description = "There was an internal server error", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
            }
    )
    @PostMapping(ApiRoutes.V1.Auth.LOGIN)
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @Operation(summary = "Register a new User and log in")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201", description = "User successfully registered and logged in", content = @Content(
                            schema = @Schema(implementation = LoginResponseDto.class)
                    )),
                    @ApiResponse(
                            responseCode = "400", description = "There was a problem with the request. One or more parameters failed some validations", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "404", description = "A certain resource has not been found", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "500", description = "There was an internal server error", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
            }
    )
    @PostMapping(ApiRoutes.V1.Auth.REGISTER)
    public ResponseEntity<LoginResponseDto> register(@Valid @RequestBody CreateUserDto createUserDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(createUserDto));
    }

    @Operation(summary = "Refresh a JWT token")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Successful operation", content = @Content(
                            schema = @Schema(implementation = LoginResponseDto.class)
                    )),
                    @ApiResponse(
                            responseCode = "400", description = "There was a problem with the request. One or more parameters failed some validations", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "404", description = "A certain resource has not been found", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "500", description = "There was an internal server error", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
            }
    )
    @PostMapping(ApiRoutes.V1.Auth.REFRESH_TOKEN)
    public ResponseEntity<LoginResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto requestDto){
        return ResponseEntity.ok(authService.refreshToken(requestDto));
    }
}
