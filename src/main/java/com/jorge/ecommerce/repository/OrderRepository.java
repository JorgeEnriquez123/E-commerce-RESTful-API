package com.jorge.ecommerce.repository;

import com.jorge.ecommerce.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"shippingAddress", "orderDetails", "orderDetails.product"})
    Page<Order> findByUserId(Pageable pageable, Long userId);
}
