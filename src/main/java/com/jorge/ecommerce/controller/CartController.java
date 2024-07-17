package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.CartDto;
import com.jorge.ecommerce.dto.create.CreateCartDto;
import com.jorge.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartDto>> findAll(){
        return ResponseEntity.ok(cartService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(cartService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CartDto> save(@RequestBody CreateCartDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.save(dto));
    }


}
