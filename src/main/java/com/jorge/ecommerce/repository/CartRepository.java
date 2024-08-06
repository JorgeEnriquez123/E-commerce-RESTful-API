package com.jorge.ecommerce.repository;

import com.jorge.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems ci " +
            "LEFT JOIN FETCH ci.product p LEFT JOIN FETCH p.category WHERE c.user.id = :userId")
    Optional<Cart> findCartWithItemsByUserId(Long userId);
}
