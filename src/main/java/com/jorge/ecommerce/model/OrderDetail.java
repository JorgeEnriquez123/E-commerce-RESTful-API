package com.jorge.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {
    @EmbeddedId
    private OrderDetailPk id;

    @MapsId("orderId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "order_id")
    @JsonBackReference
    @ToString.Exclude
    private Order order;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "product_id")
    @ToString.Exclude
    private Product product;

    private Integer quantity;
    private BigDecimal price;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Embeddable
    public static class OrderDetailPk implements Serializable {
        Long orderId;
        Long productId;
    }
}
