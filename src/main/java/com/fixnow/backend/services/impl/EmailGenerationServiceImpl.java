package com.fixnow.backend.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fixnow.backend.dtos.request.EmailRequest;
import com.fixnow.backend.dtos.response.EmailResponse;
import com.fixnow.backend.models.User;
import com.fixnow.backend.models.UserWallet;
import com.fixnow.backend.repositories.UserRepository;
import com.fixnow.backend.repositories.UserWalletRepository;
import com.fixnow.backend.services.EmailGenerationService;
import com.fixnow.backend.services.WalletService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class EmailGenerationServiceImpl implements EmailGenerationService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.url}")
    private String apiUrl;

    @Value("${spring.ai.openai.chat.options.model}")
    private String model;

    private final WebClient webClient = WebClient.builder().build();

    private final WalletService walletService;
    private final UserWalletRepository userWalletRepository;
    private final UserRepository userRepository;

    public EmailGenerationServiceImpl(WalletService walletService, UserWalletRepository userWalletRepository, UserRepository userRepository) {
        this.walletService = walletService;
        this.userWalletRepository = userWalletRepository;
        this.userRepository = userRepository;
    }

    @Override
    public EmailResponse generateEmail(EmailRequest request) {
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful assistant that writes emails."),
                        Map.of("role", "user", "content", buildPrompt(request))
                )
        );

        Map<String, Object> response = webClient.post()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        String content = ((String) message.get("content")).trim();

        String subject = "No Subject";
        String emailBody = content;

        // Tách subject theo dòng đầu tiên bắt đầu bằng "Subject:"
        String[] lines = content.split("\\r?\\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].toLowerCase().startsWith("subject:")) {
                subject = lines[i].substring("subject:".length()).trim();
                StringBuilder bodyBuilder = new StringBuilder();

                for (int j = i + 1; j < lines.length; j++) {
                    String line = lines[j];

                    // Nếu dòng này là "Body:", thì bỏ qua
                    if (line.toLowerCase().startsWith("body:")) {
                        // Nếu sau "Body:" còn nội dung, thì giữ lại phần nội dung đó
                        String bodyLine = line.substring("body:".length()).trim();
                        if (!bodyLine.isEmpty()) {
                            bodyBuilder.append(bodyLine).append("\n");
                        }
                        continue;
                    }

                    bodyBuilder.append(line).append("\n");
                }

                emailBody = bodyBuilder.toString().trim();
                break;
            }
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        int wordCount = walletService.countWords(emailBody); // Tùy bạn định nghĩa số từ cần trừ

        UserWallet wallet = userWalletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getWordQuota() < wordCount) {
            throw new RuntimeException("Không đủ từ để thực hiện thao tác");
        }

        wallet.setWordQuota(wallet.getWordQuota() - wordCount);
        userWalletRepository.save(wallet);

        EmailResponse resp = new EmailResponse();
        resp.setSubject(subject);
        resp.setEmailBody(emailBody);
        return resp;
    }



    private String buildPrompt(EmailRequest req) {
        return String.format("""
            Write an email in %s.
            Tone: %s. Style: %s. Length: %s. Mood: %s. Emoji: %s.
            Sender: %s
            Receiver: %s
            Key points to include: %s
            
            Output format:
            Subject: <email subject>
            Body: <email content>
            
            Make sure 'Subject:' and 'Body:' appear on separate lines at the beginning of the response.
            """,
                req.getLanguage(),
                req.getTone(),
                req.getStyle(),
                req.getLength(),
                req.getMood(),
                req.getEmoji(),
                req.getSender(),
                req.getReceiver(),
                req.getKeyPoints()

        );
    }

}
