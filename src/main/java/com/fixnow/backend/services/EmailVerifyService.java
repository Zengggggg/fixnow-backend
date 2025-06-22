package com.fixnow.backend.services;

import com.fixnow.backend.models.User;
import jakarta.servlet.http.HttpServletRequest;

public interface EmailVerifyService {
    void sendVerificationEmail(User user, String token, HttpServletRequest request);
}
