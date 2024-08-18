package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.annotations.RoleAdminOrCustomer;
import com.jorge.ecommerce.dto.AddressLineDto;
import com.jorge.ecommerce.dto.create.CreateAddressLineDto;
import com.jorge.ecommerce.dto.update.UpdateAddressLineDto;
import com.jorge.ecommerce.model.User;
import com.jorge.ecommerce.service.AddressLineService;
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
@Tag(name = "Address Lines", description = "User's Address lines (Also referred as shipping addresses")
public class AddressLineController {
    private final AddressLineService addressLineService;

    @GetMapping
    public ResponseEntity<List<AddressLineDto>> getAllAddressLines(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(addressLineService.getByUser(user));
    }

    @PostMapping
    public ResponseEntity<AddressLineDto> addAddressLine(@AuthenticationPrincipal User user, @RequestBody CreateAddressLineDto createAddressLineDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(addressLineService.saveAddressLine(user, createAddressLineDto));
    }

    @PutMapping(ApiRoutes.V1.AddressLine.UPDATE)
    public ResponseEntity<AddressLineDto> updateAddressLine(@AuthenticationPrincipal User user, @PathVariable Long addressLineId,
                                                            @RequestBody UpdateAddressLineDto updateAddressLineDto){
        return ResponseEntity.ok(addressLineService.updateAddressLine(user, addressLineId, updateAddressLineDto));
    }

    @PutMapping(ApiRoutes.V1.AddressLine.SET_DEFAULT)
    public ResponseEntity<AddressLineDto> setDefaultAddressLine(@AuthenticationPrincipal User user, @PathVariable Long addressLineId){
        return ResponseEntity.ok(addressLineService.setDefaultAddressLine(user, addressLineId));
    }
}
