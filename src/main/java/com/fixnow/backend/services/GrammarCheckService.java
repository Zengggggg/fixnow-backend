package com.fixnow.backend.services;

import com.fixnow.backend.dtos.response.GrammarError;

import java.util.List;

public interface GrammarCheckService {
    List<GrammarError> checkText(String text);
}
