package com.fixnow.backend.dtos.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TransationRequest {
    private String text;
    private String sourceLang;
    private String targetLang;
}
