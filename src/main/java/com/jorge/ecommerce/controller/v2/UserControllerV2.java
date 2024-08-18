package com.jorge.ecommerce.controller.v2;

import com.jorge.ecommerce.annotations.RoleAdminOrCustomer;
import com.jorge.ecommerce.controller.ApiRoutes;
import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.handler.response.ErrorResponse;
import com.jorge.ecommerce.model.AuthenticatedUserDto;
import com.jorge.ecommerce.model.Role;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(
        name = "bearerAuth"
)
@RequiredArgsConstructor
@RestController
@RequestMapping(ApiRoutes.V2.User.ROOT)
@RoleAdminOrCustomer
@Tag(name = "Users", description = "Operations about Users")
public class UserControllerV2 {

    //Example of a version 2 of getUserInfo
    @Operation(summary = "Get User Info with fewer data")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = AuthenticatedUserDto.class)
            )),
            @ApiResponse(
                    responseCode = "401", description = "The User is not authorized to perform this operation", content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(
                    responseCode = "403", description = "The User has insufficient permission to perform this operation", content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(
                    responseCode = "500", description = "There was an internal server error", content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            ))
    })
    @GetMapping(ApiRoutes.V2.User.GET_CURRENT_USER_INFO)
    public ResponseEntity<AuthenticatedUserDto> getUserInfo(@AuthenticationPrincipal User user) {
        AuthenticatedUserDto authenticatedUserDto = AuthenticatedUserDto.builder()
                .username(user.getUsername())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .toList())
                        .build();

        return ResponseEntity.ok().body(authenticatedUserDto);
    }
}
