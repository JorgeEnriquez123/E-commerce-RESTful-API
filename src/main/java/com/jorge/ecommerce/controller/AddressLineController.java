package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.AddressLineDto;
import com.jorge.ecommerce.service.AddressLineService;
import lombok.AllArgsConstructor;
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

    @PostMapping("/{idUser}")
    public ResponseEntity<AddressLineDto> saveAddressLine(@PathVariable Long idUser, @RequestBody AddressLineDto addressLineDto) {
        return ResponseEntity.ok(addressLineService.saveAddressLineWithUserId(idUser, addressLineDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressLineDto> updateAddressLine(@PathVariable Long id, @RequestBody AddressLineDto addressLineDto) {
        return ResponseEntity.ok(addressLineService.updateAddressLine(id, addressLineDto));
    }
}
