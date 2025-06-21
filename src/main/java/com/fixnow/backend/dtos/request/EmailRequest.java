package com.fixnow.backend.dtos.request;

import lombok.Data;

@Data
public class EmailRequest {
    private String sender;
    private String receiver;
    private String keyPoints;
    private String language;
    private String length;
    private String style;
    private String tone;
    private String mood;
    private String emoji;
}
