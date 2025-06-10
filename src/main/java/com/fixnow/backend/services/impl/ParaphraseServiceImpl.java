package com.fixnow.backend.services.impl;

import com.fixnow.backend.services.ParaphraseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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

    @Override
    public String paraphrase(String inputText) {
        Map<String, Object> body = Map.of(
                "model", "gpt-4",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful assistant that paraphrases English text."),
                        Map.of("role", "user", "content", "Paraphrase this: " + inputText)
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

            return content.trim();

        }  catch (Exception e) {
            return "Failed to paraphrase: " + e.getMessage();
        }

    }
}
