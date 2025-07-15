package com.fixnow.backend.services.impl;

import com.fixnow.backend.dtos.request.ParaphraseRequest;
import com.fixnow.backend.models.User;
import com.fixnow.backend.models.UserWallet;
import com.fixnow.backend.repositories.UserRepository;
import com.fixnow.backend.repositories.UserWalletRepository;
import com.fixnow.backend.services.ParaphraseService;
import com.fixnow.backend.services.WalletService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class ParaphraseServiceImpl implements ParaphraseService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.url}")
    private String apiUrl;

    private final WebClient webClient = WebClient.builder().build();

    private final UserRepository userRepository;
    private final UserWalletRepository walletRepository;
    private final WalletService walletService;

    public ParaphraseServiceImpl(UserRepository userRepository, UserWalletRepository walletRepository, WalletService walletService) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.walletService = walletService;
    }

    @Override
    public String paraphrase(ParaphraseRequest request) {
        Map<String, Object> body = Map.of(
                "model", "gpt-4",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful assistant that paraphrases English text."),
                        Map.of("role", "user", "content", buildPrompt(request))
                )
        );

        try {
            Map response = webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();


            List choices = (List) response.get("choices");
            if (choices == null || choices.isEmpty()) {
                return "No paraphrased content found.";
            }

            Map choice = (Map) choices.get(0);
            Map message = (Map) choice.get("message");
            String content = (String) message.get("content");

            if (content == null) {
                return "Paraphrased content is null.";
            }
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            String result = content.trim();


            int wordCount = walletService.countWords(result);


            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
            UserWallet wallet = walletRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Wallet not found"));

            if (wallet.getWordQuota() < wordCount) {
                throw new IllegalStateException("Không đủ từ để thực hiện paraphrase.");
            }

            wallet.setWordQuota(wallet.getWordQuota() - wordCount);
            walletRepository.save(wallet);
            return content.trim();

        }  catch (Exception e) {
            return "Failed to paraphrase: " + e.getMessage();
        }

    }

    private String buildPrompt(ParaphraseRequest req) {
        return String.format("""
        Paraphrase the following text in %s.
        Make the output natural, fluent, and easy to understand while preserving the original meaning.
        
        Text:
        %s
        """, req.getLanguage(), req.getText());
    }

}
