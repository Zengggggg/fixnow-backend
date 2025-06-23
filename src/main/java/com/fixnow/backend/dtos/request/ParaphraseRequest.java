package com.fixnow.backend.dtos.request;

import lombok.Data;

@Data
public class ParaphraseRequest {
    private String language;
    private String text;
}
