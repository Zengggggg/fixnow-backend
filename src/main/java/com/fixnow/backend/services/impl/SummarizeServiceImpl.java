package com.fixnow.backend.services.impl;

import com.fixnow.backend.services.SummarizeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class SummarizeServiceImpl implements SummarizeService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.url}")
    private String apiUrl;

    private final WebClient webClient = WebClient.builder().build();


    @Override
    public String summarize(String input, int length, String format) {
        String prompt = buildPrompt(input, length, format);


        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
        );

        Map response = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block(); // Blocking call for simplicity, suitable for controller logic

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

        return (String) message.get("content");
    }
    private String buildPrompt(String input, int length, String format) {
        if ("bullet".equalsIgnoreCase(format)) {
            return "Summarize the following text into bullet points:\n" + input;
        }

        // Convert length to readable English
        String readableLength;
        switch (length) {
            case 1:
                readableLength = "short (1-2 sentences)";
                break;
            case 2:
                readableLength = "medium (1 paragraph)";
                break;
            case 3:
                readableLength = "long (2-3 paragraphs)";
                break;
            case 4:
                readableLength = "very long (full-length summary)";
                break;
            default:
                readableLength = "short";
        }

        return "Summarize the following text in a " + readableLength + ":\n" + input;
    }
}
