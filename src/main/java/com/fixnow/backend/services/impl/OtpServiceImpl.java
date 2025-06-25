package com.fixnow.backend.services.impl;

import com.fixnow.backend.models.OtpVerification;
import com.fixnow.backend.repositories.OtpRepository;
import com.fixnow.backend.services.OtpService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;
    @Value("${resend.api.key}")
    private String resendApiKey;
    @Value("${resend.from.email}")
    private String resendFromEmail;
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.resend.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public OtpServiceImpl(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }
    @Transactional
    @Override
    public void generateAndSendOtp(String email) {
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
        LocalDateTime expireAt = LocalDateTime.now().plusMinutes(5);

        otpRepository.deleteByEmail(email); // Xoá OTP cũ (nếu có)

        OtpVerification otpEntity = new OtpVerification();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpirationTime(expireAt);
        otpRepository.save(otpEntity);

        sendOtpViaResend(email, otp);
    }

    @Override
    public void sendOtpViaResend(String toEmail, String otp) {
        Map<String, Object> payload = Map.of(
                "from", resendFromEmail,  // hoặc domain của bạn nếu đã xác thực
                "to", toEmail,
                "subject", "Mã OTP khôi phục mật khẩu",
                "text", "Mã OTP của bạn là: " + otp + "\nHiệu lực trong 5 phút."
        );

        webClient.post()
                .uri("/emails")
                .header("Authorization", "Bearer " + resendApiKey)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> System.err.println("Lỗi gửi email: " + e.getMessage()))
                .subscribe();
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        Optional<OtpVerification> otpRecord = otpRepository.findTopByEmailOrderByExpirationTimeDesc(email);

        return otpRecord.isPresent()
                && otpRecord.get().getOtp().equals(otp)
                && otpRecord.get().getExpirationTime().isAfter(LocalDateTime.now());
    }
}
