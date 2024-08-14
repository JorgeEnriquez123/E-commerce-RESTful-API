package com.jorge.ecommerce.repository;

import com.jorge.ecommerce.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Cart will be fetched regardless due to it being a @OneToOne, so I'm including it to make it a single query.
    @EntityGraph(attributePaths = {"cart", "roles"})
    @Override
    Optional<User> findById(Long id);

    @EntityGraph(attributePaths = {"cart", "roles"})
    @Override
    Page<User> findAll(Pageable pageable);


    @EntityGraph(attributePaths = {"cart", "roles"})
    Optional<User> findByUsername(String username);

    //To check if there are users with a set Username with a more optimized query (Used for validation to avoid duplicated usernames)
    @Query("SELECT COUNT(u) FROM User u WHERE u.username = :username")
    long countUsersByUsername(String username);
}
