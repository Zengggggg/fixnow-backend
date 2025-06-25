package com.fixnow.backend.repositories;

import com.fixnow.backend.models.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findTopByEmailOrderByExpirationTimeDesc(String email);
    void deleteByEmail(String email);

}
