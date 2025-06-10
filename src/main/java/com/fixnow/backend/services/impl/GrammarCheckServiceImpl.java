package com.fixnow.backend.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fixnow.backend.dtos.response.GrammarError;
import com.fixnow.backend.services.GrammarCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GrammarCheckServiceImpl implements GrammarCheckService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.url}")
    private String apiUrl;

    private final WebClient webClient = WebClient.builder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public List<GrammarError> checkText(String text) {
        String prompt = buildPrompt(text);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are an English grammar and spelling checker."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.2
        );

        try {
            String response = webClient.post()
                    .uri(apiUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Blocking for simplicity; change to reactive if needed

            return parseAndEnrichErrors(text,response);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    private String buildPrompt(String input) {
        return """
        You are a grammar and spelling checker.
    
        Given the following English text, identify all grammar and spelling errors.
    
        For each error, return a JSON object with:
        - error: the incorrect word or phrase.
        - type: either "spelling" or "grammar".
        - suggestion: the corrected word or phrase.
    
        Return only a JSON array, no explanation.
    
        Text: "%s"
        """.formatted(input);
    }

    private List<GrammarError> parseAndEnrichErrors(String input, String response) {
        try {
            // Parse JSON string from GPT response
            JsonNode root = objectMapper.readTree(response);
            String content = root.path("choices").get(0).path("message").path("content").asText();

            // Parse list of GrammarError objects
            List<GrammarError> errors = objectMapper.readValue(content, new TypeReference<>() {});

            // Enrich with start/end index
            for (GrammarError err : errors) {
                String errorText = err.getError();
                int index = input.indexOf(errorText);

                // Tìm vị trí đầu tiên (cơ bản)
                if (index >= 0) {
                    err.setStart(index);
                    err.setEnd(index + errorText.length());
                } else {
                    err.setStart(-1);
                    err.setEnd(-1);
                }
            }

            return errors;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
