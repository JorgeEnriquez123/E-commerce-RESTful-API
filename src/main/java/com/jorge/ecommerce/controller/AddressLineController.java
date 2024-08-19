package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.annotations.RoleAdminOrCustomer;
import com.jorge.ecommerce.dto.AddressLineDto;
import com.jorge.ecommerce.dto.create.CreateAddressLineDto;
import com.jorge.ecommerce.dto.pagination.PaginatedAddressLineResponse;
import com.jorge.ecommerce.dto.update.UpdateAddressLineDto;
import com.jorge.ecommerce.handler.response.ErrorResponse;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.service.AddressLineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(
        name = "bearerAuth"
)
@RestController
@RequestMapping(ApiRoutes.V1.AddressLine.ROOT)
@RequiredArgsConstructor
@RoleAdminOrCustomer
@Tag(name = "Address Lines", description = "User's Address lines (Also referred as shipping addresses)")
public class AddressLineController {
    private final AddressLineService addressLineService;

    @Operation(summary = "Get all Address lines from current User")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Successful operation", content = @Content(
                            schema = @Schema(implementation = PaginatedAddressLineResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "401", description = "The User is not authorized to access this", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "403", description = "The User has insufficient permission to access this", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "500", description = "There was an internal server error", content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
            }
    )
    @GetMapping
    public ResponseEntity<List<AddressLineDto>> getAllAddressLines(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(addressLineService.getByUser(user));
    }

    @Operation(summary = "Save a new Address line to current User")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201", description = "Resource successfully created", content = @Content(
                            schema = @Schema(implementation = AddressLineDto.class)
                    )),
                    @ApiResponse(
                            responseCode = "400", description = "There was a problem with the request. One or more parameters failed some validations", content = @Content(
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
    @PostMapping
    public ResponseEntity<AddressLineDto> addAddressLine(@AuthenticationPrincipal User user, @RequestBody CreateAddressLineDto createAddressLineDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(addressLineService.saveAddressLine(user, createAddressLineDto));
    }

    @Operation(summary = "Update an Address line's info of current User")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Successful operation", content = @Content(
                            schema = @Schema(implementation = AddressLineDto.class)
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
    @PutMapping(ApiRoutes.V1.AddressLine.UPDATE)
    public ResponseEntity<AddressLineDto> updateAddressLine(@AuthenticationPrincipal User user, @PathVariable Long addressLineId,
                                                            @RequestBody UpdateAddressLineDto updateAddressLineDto){
        return ResponseEntity.ok(addressLineService.updateAddressLine(user, addressLineId, updateAddressLineDto));
    }

    @Operation(summary = "Remove an Address line from current User")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Successful operation without content", content = @Content(
                            schema = @Schema(hidden = true)
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
    @DeleteMapping(ApiRoutes.V1.AddressLine.REMOVE)
    public ResponseEntity<Void> removeAddressLine(@AuthenticationPrincipal User user, @PathVariable Long addressLineId){
        addressLineService.removeAddressLine(user, addressLineId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Set a Address line from current User as default")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Successful operation", content = @Content(
                            schema = @Schema(implementation = AddressLineDto.class)
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
    @PutMapping(ApiRoutes.V1.AddressLine.SET_DEFAULT)
    public ResponseEntity<AddressLineDto> setDefaultAddressLine(@AuthenticationPrincipal User user, @PathVariable Long addressLineId){
        return ResponseEntity.ok(addressLineService.setDefaultAddressLine(user, addressLineId));
    }
}
