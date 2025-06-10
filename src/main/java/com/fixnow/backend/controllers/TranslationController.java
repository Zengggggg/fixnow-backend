package com.fixnow.backend.controllers;

import com.fixnow.backend.services.TranslationService;
import com.fixnow.backend.dtos.request.TransationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TranslationController {
    private final TranslationService translationService;

    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping("/translation")
    public Map<String, String> translate(@RequestBody Map<String, String> body) {
        String text = body.get("text");
        String targetLang = body.get("targetLang");
        String result = translationService.translate(text, targetLang);
        return Map.of("translation", result);
    }
}
