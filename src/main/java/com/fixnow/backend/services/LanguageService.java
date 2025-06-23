package com.fixnow.backend.services;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class LanguageService {

    private final WebClient webClient = WebClient.builder().build();

    @Value("${spring.ai.openai.url}")
    private String apiUrl;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.chat.options.model}")
    private String chatOptionsModel;


    public String detectLanguage(String text) {
        String prompt = "What language is this text written in? Just return the language name.\n\n" + text;

        Map<String, Object> request = Map.of(
                "model", chatOptionsModel,
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a language detection engine."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0
        );

        try {
            JsonNode root = webClient.post()
                    .uri(apiUrl)
                    .header("Authorization","Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            return root.path("choices").get(0).path("message").path("content").asText().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }
}
