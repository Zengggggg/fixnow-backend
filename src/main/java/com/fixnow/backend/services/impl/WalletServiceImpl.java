package com.fixnow.backend.services.impl;

import com.fixnow.backend.models.TransactionHistory;
import com.fixnow.backend.models.User;
import com.fixnow.backend.models.UserWallet;
import com.fixnow.backend.repositories.TransactionHistoryRepository;
import com.fixnow.backend.repositories.UserWalletRepository;
import com.fixnow.backend.services.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final UserWalletRepository walletRepository;
    private final TransactionHistoryRepository historyRepository;

    @Override
    public void purchaseWords(User user, int words, double cost) {
        UserWallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getBalance() < cost) {
            throw new RuntimeException("Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance() - cost);
        wallet.setWordQuota(wallet.getWordQuota() + words);
        walletRepository.save(wallet);

        TransactionHistory history = new TransactionHistory();
        history.setUser(user);
        history.setType("PURCHASE_WORD");
        history.setWordsChanged(words);
        history.setAmountChanged(-cost);
        history.setDescription("Purchased " + words + " words");
        history.setTimestamp(LocalDateTime.now());
        historyRepository.save(history);
    }

    @Override
    public void consumeWords(User user, int wordsToConsume) {
        UserWallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getWordQuota() < wordsToConsume) {
            throw new RuntimeException("Insufficient word quota");
        }

        wallet.setWordQuota(wallet.getWordQuota() - wordsToConsume);
        walletRepository.save(wallet);

        TransactionHistory history = new TransactionHistory();
        history.setUser(user);
        history.setType("USAGE");
        history.setWordsChanged(-wordsToConsume);
        history.setAmountChanged(0.0);
        history.setDescription("Used " + wordsToConsume + " words");
        history.setTimestamp(LocalDateTime.now());
        historyRepository.save(history);
    }

    @Override
    public UserWallet getWallet(User user) {
        return walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    @Override
    public void topUp(User user, double amount) {
        UserWallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);

        TransactionHistory history = new TransactionHistory();
        history.setUser(user);
        history.setType("TOPUP");
        history.setAmountChanged(amount);
        history.setWordsChanged(0);
        history.setDescription("Top-up via admin confirmation");
        history.setTimestamp(LocalDateTime.now());
        historyRepository.save(history);
    }

    @Override
    public int countWords(String text) {
        if (text == null || text.trim().isEmpty()) return 0;

        // Loại bỏ dấu câu, ký tự đặc biệt, giữ lại từ
        String cleaned = text.replaceAll("[^\\p{L}\\p{Nd}]+", " ").trim();

        if (cleaned.isEmpty()) return 0;

        // Tách từ bằng khoảng trắng
        String[] words = cleaned.split("\\s+");

        return words.length;
    }
}
