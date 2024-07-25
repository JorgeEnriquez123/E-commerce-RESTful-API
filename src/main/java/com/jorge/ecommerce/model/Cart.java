package com.jorge.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jorge.ecommerce.dto.CartDto;
import com.jorge.ecommerce.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart")
    @JsonManagedReference
    private Set<CartItem> cartItems = new HashSet<>();
}
