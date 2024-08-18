package com.jorge.ecommerce.repository;

import com.jorge.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItem.CartItemPk> {
    @EntityGraph(attributePaths = {"product", "product.category"})
    List<CartItem> findByCartId(Long cartId);

    // Native query is used to avoid a SELECT query that hibernate runs before the insert
    @Modifying
    @Query(value = "INSERT INTO cart_item (cart_id, product_id, quantity) " +
            "VALUES (:#{#cartItem.cart.id}, :#{#cartItem.product.id}, :#{#cartItem.quantity})", nativeQuery = true)
    void insertCartItem(@Param("cartItem") CartItem cartItem);
}
