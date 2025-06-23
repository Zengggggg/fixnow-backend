package com.fixnow.backend.services;

import com.fixnow.backend.dtos.request.ParaphraseRequest;

public interface ParaphraseService {
    String paraphrase(ParaphraseRequest request);
}
