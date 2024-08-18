package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.annotations.RoleAdmin;
import com.jorge.ecommerce.annotations.RoleAdminOrCustomer;
import com.jorge.ecommerce.dto.UserDto;
import com.jorge.ecommerce.dto.auth.AddRoleToUserDto;
import com.jorge.ecommerce.dto.pagination.PaginatedUserResponse;
import com.jorge.ecommerce.dto.update.UpdateUserDto;
import com.jorge.ecommerce.handler.response.ErrorResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(
        name = "bearerAuth"
)
@RequiredArgsConstructor
@RestController
@RequestMapping(ApiRoutes.V1.User.ROOT)
@RoleAdminOrCustomer
@Tag(name = "Users", description = "Operations about Users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get Current User's information")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Successful operation", content = @Content(
                                    schema = @Schema(implementation = UserDto.class)
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
            }
    )
    @GetMapping(ApiRoutes.V1.User.GET_CURRENT_USER_INFO)
    public ResponseEntity<UserDto> getUserInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(userService.getUserInfo(user));
    }

    @Operation(summary = "Update current authenticated User")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Successful operation", content = @Content(
                            schema = @Schema(implementation = UserDto.class)
                    )),
                    @ApiResponse(
                            responseCode = "400", description = "There was a problem with the request. One or more parameteres failed some validations", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
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
            }
    )
    @PutMapping
    public ResponseEntity<UserDto> updateUserPersonalInfo(@AuthenticationPrincipal User user, @RequestBody UpdateUserDto updateUserDto){
        return ResponseEntity.ok().body(userService.updateUserPersonalInfo(user, updateUserDto));
    }

    // -----------

    @Operation(summary = "Get all Users with pagination")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Successful operation", content = @Content(
                            schema = @Schema(implementation = PaginatedUserResponse.class)
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
            }
    )
    @RoleAdmin
    @GetMapping
    public ResponseEntity<Page<UserDto>> getAll(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer size,
                                                @RequestParam(defaultValue = "asc") String sortOrder,
                                                @RequestParam(defaultValue = "id") String sortBy){
        return ResponseEntity.ok(userService.findAll(page, size, sortOrder, sortBy));
    }

    @Operation(summary = "Get User by Id")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Successful operation", content = @Content(
                            schema = @Schema(implementation = UserDto.class)
                    )),
                    @ApiResponse(
                            responseCode = "404", description = "A certain resource has not been found", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
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
            }
    )
    @RoleAdmin
    @GetMapping(ApiRoutes.V1.User.GET_BY_ID)
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @Operation(summary = "Add Role to User")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Successful operation", content = @Content(
                            schema = @Schema(implementation = UserDto.class)
                    )),
                    @ApiResponse(
                            responseCode = "400", description = "There was a problem with the request. One or more parameteres failed some validations", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "404", description = "A certain resource has not been found", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
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
            }
    )
    @RoleAdmin
    @PostMapping(ApiRoutes.V1.User.POST_ADD_ROLE)
    public ResponseEntity<UserDto> addRoleToUser(@PathVariable Long userId, @RequestBody AddRoleToUserDto addRoleToUserDto){
        return ResponseEntity.ok(userService.addRoleToUser(userId, addRoleToUserDto));
    }

    @Operation(summary = "Remove Role from User")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Successful operation", content = @Content(
                            schema = @Schema(implementation = UserDto.class)
                    )),
                    @ApiResponse(
                            responseCode = "404", description = "A certain resource has not been found", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
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
            }
    )
    @RoleAdmin
    @DeleteMapping(ApiRoutes.V1.User.DELETE_ROLE)
    public ResponseEntity<UserDto> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId){
        return ResponseEntity.ok(userService.deleteRoleFromUser(userId, roleId));
    }
}
