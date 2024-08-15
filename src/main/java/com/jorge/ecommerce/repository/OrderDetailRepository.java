package com.jorge.ecommerce.repository;

import com.jorge.ecommerce.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Modifying
    @Query(nativeQuery = true,
    value = "INSERT INTO order_detail (order_id, product_id, quantity, price) VALUES (:#{#orderDetail.order.id}, :#{#orderDetail.product.id}, " +
            ":#{#orderDetail.quantity}, :#{#orderDetail.price})")
    void insertOrderDetail(@Param("orderDetail") OrderDetail orderDetail);
}
