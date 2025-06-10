package com.fixnow.backend.dtos.response;

import lombok.Data;

@Data
public class ParaphraseResponse {
    private String paraphrasedText;

    public ParaphraseResponse(String paraphrasedText) {
        this.paraphrasedText = paraphrasedText;
    }

}
