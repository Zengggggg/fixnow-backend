package com.fixnow.backend.services;

public interface OtpService {
    void generateAndSendOtp(String email);
    void sendOtpViaResend(String toEmail, String otp);
    boolean verifyOtp(String email, String otp);
}
