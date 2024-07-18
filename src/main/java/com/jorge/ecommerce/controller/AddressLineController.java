package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.AddressLineDto;
import com.jorge.ecommerce.dto.create.CreateAddressLineDto;
import com.jorge.ecommerce.dto.update.UpdateAddressLineDto;
import com.jorge.ecommerce.service.AddressLineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/addressLine")
public class AddressLineController {
    private final AddressLineService addressLineService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<AddressLineDto>> getAddressLineByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(addressLineService.getAddressesByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<AddressLineDto> saveAddressLine(@Valid @RequestBody CreateAddressLineDto createAddressLineDto) {
        return ResponseEntity.ok(addressLineService.saveAddressLineWithUserId(createAddressLineDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressLineDto> updateAddressLine(@PathVariable Long id, @Valid @RequestBody UpdateAddressLineDto updateAddressLineDto) {
        return ResponseEntity.ok(addressLineService.updateAddressLine(id, updateAddressLineDto));
    }

    @PutMapping("/setDefaultAddressLine/{userId}/{addressLineId}")
    public ResponseEntity<Void> setDefaultAddressLineOfUser(@PathVariable Long userId, @PathVariable Long addressLineId) {
        addressLineService.setDefaultAddressLineOfUser(userId, addressLineId);
        return ResponseEntity.noContent().build();
    }
}
