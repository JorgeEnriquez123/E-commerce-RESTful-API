package com.jorge.ecommerce.repository;

import com.jorge.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.shippingAddress sa LEFT JOIN FETCH o.orderDetails od " +
            "LEFT JOIN FETCH od.product p LEFT JOIN FETCH p.category WHERE o.user.id = :userId")
    Optional<List<Order>> findOrdersWithDetailsByUserId(Long userId);
}
