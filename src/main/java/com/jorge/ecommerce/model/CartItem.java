package com.jorge.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    @EmbeddedId
    private CartItemPk id;

    @MapsId("cartId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "cart_id")
    @JsonIgnore
    @ToString.Exclude
    private Cart cart;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "product_id")
    @JsonIgnore
    @ToString.Exclude
    private Product product;

    @Column(nullable = false)
    private Integer quantity;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Embeddable
    public static class CartItemPk implements Serializable {
        Long cartId;
        Long productId;
    }
}
