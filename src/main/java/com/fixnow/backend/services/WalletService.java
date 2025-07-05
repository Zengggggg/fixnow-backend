package com.fixnow.backend.services;

import com.fixnow.backend.models.User;
import com.fixnow.backend.models.UserWallet;

public interface WalletService {

    void purchaseWords(User user, int words, double cost);
    void consumeWords(User user, int wordsToConsume);
    UserWallet getWallet(User user);

}
