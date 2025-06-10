package com.fixnow.backend.services;

import reactor.core.publisher.Mono;

public interface TranslationService {
    String translate(String sourceText, String targetLanguage);
}
