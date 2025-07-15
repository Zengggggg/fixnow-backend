package com.fixnow.backend.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fixnow.backend.dtos.request.TransationRequest;
import com.fixnow.backend.dtos.response.TranslationResponse;
import com.fixnow.backend.models.User;
import com.fixnow.backend.models.UserWallet;
import com.fixnow.backend.repositories.UserRepository;
import com.fixnow.backend.repositories.UserWalletRepository;
import com.fixnow.backend.services.TranslationService;
import com.fixnow.backend.services.WalletService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranslationServiceImpl implements TranslationService {
    @Value("${spring.ai.openai.url}")
    private String apiUrl;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    private final WebClient webClient = WebClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UserRepository userRepository;
    private final UserWalletRepository walletRepository;
    private final WalletService walletService;

    public TranslationServiceImpl(UserRepository userRepository, UserWalletRepository walletRepository, WalletService walletService) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.walletService = walletService;
    }

    public String translate(String sourceText, String targetLanguage) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        try {


            String prompt = String.format("Translate the following text to %s:\n%s", targetLanguage, sourceText);

            Map<String, Object> requestBody = Map.of(
                    "model", "gpt-4",
                    "messages", List.of(
                            Map.of("role", "system", "content", "You are a translation engine."),
                            Map.of("role", "user", "content", prompt)
                    ),
                    "temperature", 0
            );

            String response = webClient.post()
                    .uri(apiUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);
            String translated = root.path("choices").get(0).path("message").path("content").asText().trim();

            // Đếm từ của kết quả dịch
            int wordCount = walletService.countWords(translated);

            // Lấy user và wallet
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

            UserWallet wallet = walletRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Wallet not found"));

            // Trừ từ
            if (wallet.getWordQuota() < wordCount) {
                throw new IllegalStateException("Không đủ từ để thực hiện dịch.");
            }

            wallet.setWordQuota(wallet.getWordQuota() - wordCount);
            walletRepository.save(wallet);

            return root.path("choices").get(0).path("message").path("content").asText().trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Translation failed.";
        }
    }

}
