package com.jorge.ecommerce.controller.v2;

import com.jorge.ecommerce.annotations.RoleAdminOrCustomer;
import com.jorge.ecommerce.controller.ApiRoutes;
import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.model.AuthenticatedUserDto;
import com.jorge.ecommerce.model.Role;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
public class UserControllerV2 {

    //Example of a version 2 of getUserInfo
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
