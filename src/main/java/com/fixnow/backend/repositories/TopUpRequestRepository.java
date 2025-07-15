package com.fixnow.backend.repositories;

import com.fixnow.backend.models.TopUpRequest;
import com.fixnow.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopUpRequestRepository extends JpaRepository<TopUpRequest, Long> {
    List<TopUpRequest> findByConfirmedFalse();
    List<TopUpRequest> findByUser(User user);
}

