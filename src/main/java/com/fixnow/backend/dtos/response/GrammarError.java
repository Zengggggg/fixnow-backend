package com.fixnow.backend.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrammarError {
    private String error;
    private String type; // "spelling" hoặc "grammar"
    private String suggestion;
    private int start;
    private int end;
}
