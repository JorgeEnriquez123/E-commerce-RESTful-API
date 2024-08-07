package com.jorge.ecommerce.repository;

import com.jorge.ecommerce.model.AddressLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressLineRepository extends JpaRepository<AddressLine, Long> {
    Optional<List<AddressLine>> findByUserId(Long userId);

    Optional<AddressLine> findByIdAndUserId(Long id, Long userId);
}
