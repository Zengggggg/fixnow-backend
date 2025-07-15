package com.fixnow.backend.controllers;

import com.fixnow.backend.models.TopUpRequest;
import com.fixnow.backend.models.User;
import com.fixnow.backend.models.UserWallet;
import com.fixnow.backend.repositories.TopUpRequestRepository;
import com.fixnow.backend.services.UserService;
import com.fixnow.backend.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final UserService userService;
    private final TopUpRequestRepository requestRepo;
    @GetMapping("/myWallet")
    public String getWalletPage(Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());
        UserWallet wallet = walletService.getWallet(user);
        model.addAttribute("wallet", wallet);
        model.addAttribute("user", user);

        return "features/wallet";
    }

    @PostMapping("/purchase")
    public String purchaseWords(@RequestParam int words,
                                @RequestParam double cost,
                                Principal principal,
                                Model model) {
        try {
            User user = userService.findByEmail(principal.getName());
            walletService.purchaseWords(user, words, cost);
            model.addAttribute("success", "Mua gói từ thành công!");
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
        }

        User user = userService.findByEmail(principal.getName());
        model.addAttribute("wallet", walletService.getWallet(user));
        return "features/wallet";
    }


}
