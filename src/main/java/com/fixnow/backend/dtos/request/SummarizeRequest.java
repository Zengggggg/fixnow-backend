package com.fixnow.backend.dtos.request;

import lombok.Data;

@Data
public class SummarizeRequest {
    private String text;
    private int length;
    private String format;

}
