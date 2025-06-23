package com.fixnow.backend.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fixnow.backend.dtos.request.TransationRequest;
import com.fixnow.backend.dtos.response.TranslationResponse;
import com.fixnow.backend.services.TranslationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    public String translate(String sourceText, String targetLanguage) {
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
            return root.path("choices").get(0).path("message").path("content").asText().trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Translation failed.";
        }
    }

}
