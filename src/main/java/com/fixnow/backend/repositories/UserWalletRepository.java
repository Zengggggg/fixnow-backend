package com.fixnow.backend.repositories;

import com.fixnow.backend.models.User;
import com.fixnow.backend.models.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {
    Optional<UserWallet> findByUser(User user);

}
