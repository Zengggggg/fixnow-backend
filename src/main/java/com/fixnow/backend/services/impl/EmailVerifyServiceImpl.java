package com.fixnow.backend.services.impl;

import com.fixnow.backend.models.User;
import com.fixnow.backend.services.EmailVerifyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailVerifyServiceImpl implements EmailVerifyService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${resend.from.email}")
    private String fromEmail;  // ví dụ: "FixNow <noreply@fixnow.site>"

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendVerificationEmail(User user, String token, HttpServletRequest request) {
        String appUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        String link = appUrl + "/verify?token=" + token;

        String subject = "Xác minh tài khoản FixNow";

        String html = """
        <div style="font-family: Arial, sans-serif; color: #333; line-height: 1.6;">
            <p>Xin chào,</p>
            <p>Cảm ơn bạn đã đăng ký tài khoản tại <strong>FixNow</strong>!</p>
            <p>Vui lòng nhấn vào nút bên dưới để <strong>xác minh địa chỉ email</strong> của bạn:</p>
            <p>
                <a href="%s" style="display: inline-block; padding: 10px 20px; background-color: #007bff;
                    color: #ffffff; text-decoration: none; border-radius: 5px;">
                    Kích hoạt tài khoản
                </a>
            </p>
            <p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.</p>
            <p>Trân trọng,<br/>Đội ngũ FixNow</p>
        </div>
        """.formatted(link);

        Map<String, Object> body = new HashMap<>();
        body.put("from", fromEmail); // ví dụ: FixNow <noreply@fixnow.io.vn>
        body.put("to", user.getEmail());
        body.put("subject", subject);
        body.put("html", html); // 👈 Gửi HTML thay vì text


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(resendApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.resend.com/emails", entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send email via Resend: " + response.getBody());
        }
    }
}
