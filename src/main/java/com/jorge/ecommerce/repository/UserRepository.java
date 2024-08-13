package com.jorge.ecommerce.repository;

import com.jorge.ecommerce.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Cart will be fetched regardless due to @OneToOne, so I'm including it to make it a single query.
    @EntityGraph(attributePaths = {"cart", "roles"})
    @Override
    Optional<User> findById(Long id);

    @EntityGraph(attributePaths = {"cart", "roles"})
    Optional<User> findByUsername(String username);
}
