package com.fixnow.backend.config;

import com.fixnow.backend.models.UserWallet;
import com.fixnow.backend.repositories.UserRepository;
import com.fixnow.backend.repositories.UserWalletRepository;
import com.fixnow.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserWalletInitializer implements CommandLineRunner {

    private final UserWalletRepository userWalletRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        userRepository.findAll().forEach(user -> {
            if (user.getWallet() == null) {
                UserWallet wallet = new UserWallet();
                wallet.setUser(user);
                wallet.setBalance(0.0);
                wallet.setWordQuota(0);
                userWalletRepository.save(wallet); // nhờ cascade nên user sẽ nhận được wallet
                System.out.println("🟢 Created wallet for user: " + user.getEmail());
            }
        });

        System.out.println("✅ Wallet initialization complete.");
    }

}
