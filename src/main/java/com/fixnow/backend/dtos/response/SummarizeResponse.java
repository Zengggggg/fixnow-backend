package com.fixnow.backend.dtos.response;

import lombok.Data;

@Data
public class SummarizeResponse {
    private String summarizedText;

    public SummarizeResponse(String sumarizedText) {
        this.summarizedText = sumarizedText;
    }
}
