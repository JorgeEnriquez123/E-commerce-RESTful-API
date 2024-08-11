package com.jorge.ecommerce.repository;

import com.jorge.ecommerce.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //@EntityGraph(attributePaths = {"cart", "roles"})
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.cart c LEFT JOIN FETCH u.roles r WHERE u.username = :username")
    Optional<User> findByUsername(String username);
}
