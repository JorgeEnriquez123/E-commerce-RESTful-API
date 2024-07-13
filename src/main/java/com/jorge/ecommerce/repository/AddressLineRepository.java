package com.jorge.ecommerce.repository;

import com.jorge.ecommerce.model.AddressLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressLineRepository extends JpaRepository<AddressLine, Long> {
}
