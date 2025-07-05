package com.fixnow.backend.repositories;

import com.fixnow.backend.models.TransactionHistory;
import com.fixnow.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    List<TransactionHistory> findByUser(User user);
}
