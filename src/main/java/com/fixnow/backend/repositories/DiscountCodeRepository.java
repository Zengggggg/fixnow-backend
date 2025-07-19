package com.fixnow.backend.repositories;

import com.fixnow.backend.models.DiscountCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountCodeRepository extends JpaRepository<DiscountCode, String> {
    Optional<DiscountCode> findByCodeIgnoreCase(String code);
}
