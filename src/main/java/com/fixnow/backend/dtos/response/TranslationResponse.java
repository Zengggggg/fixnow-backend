package com.fixnow.backend.dtos.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TranslationResponse {
    private String translatedText;
}
