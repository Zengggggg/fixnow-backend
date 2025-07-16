package com.fixnow.backend.services;

public interface TopupRequestService {
    void confirmTopupRequest(Long id);
    void cancelTopupRequest(Long id);
}