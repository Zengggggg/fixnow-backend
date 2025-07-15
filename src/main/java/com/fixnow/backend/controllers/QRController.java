package com.fixnow.backend.controllers;

import com.fixnow.backend.models.TopUpRequest;
import com.fixnow.backend.models.User;
import com.fixnow.backend.repositories.TopUpRequestRepository;
import com.fixnow.backend.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")

public class QRController {

    private final UserService userService;
    private final TopUpRequestRepository topUpRequestRepository;

    private static final String BANK_CODE = "TPB";
    private static final String ACCOUNT_NUMBER = "00000116625";
    private static final String ACCOUNT_NAME = "HOANG VINH GIANG";

    public QRController(UserService userService, TopUpRequestRepository topUpRequestRepository) {
        this.userService = userService;
        this.topUpRequestRepository = topUpRequestRepository;
    }

    @GetMapping("/qr")
    public Map<String, String> getPaymentQr(@RequestParam String username, @RequestParam double amount) {

        User user = userService.findByEmail(username); //username này thực ra l email
        String content = "fixnow-" + user.getUsername(); // đây mới l username thật
        String encodedInfo = UriUtils.encode(content, StandardCharsets.UTF_8);
        String encodedName = UriUtils.encode(ACCOUNT_NAME, StandardCharsets.UTF_8);

        String qrUrl = String.format(
                "https://img.vietqr.io/image/%s-%s-compact.png?amount=%.0f&addInfo=%s&accountName=%s",
                BANK_CODE,
                ACCOUNT_NUMBER,
                amount,
                encodedInfo,
                encodedName
        );
        TopUpRequest request = new TopUpRequest(
                user,
                amount,
                LocalDateTime.now(),
                false,
                null
        );
        topUpRequestRepository.save(request);

        return Map.of(
                "qrUrl", qrUrl,
                "bank", "TPBank",
                "accountNumber", ACCOUNT_NUMBER,
                "accountName", ACCOUNT_NAME,
                "amount", String.valueOf((int) amount),
                "transferContent", content
        );
    }

}
