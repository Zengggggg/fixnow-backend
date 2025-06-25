package com.fixnow.backend.controllers;

import com.fixnow.backend.services.OtpService;
import com.fixnow.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final UserService userService;

    private final OtpService otpService;

    @GetMapping("/forgot-password")
    public String showEmailForm() {
        return "auth/forgot-password";
    }

    @PostMapping("/do-forgot-password")
    public String processEmail(@RequestParam("email") String email, Model model) {
        if (!userService.existsByEmail(email)) {
            model.addAttribute("error", "Email chưa được đăng ký.");
            return "auth/forgot-password";
        }

        otpService.generateAndSendOtp(email);
        model.addAttribute("email", email);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String processReset(
            @RequestParam("email") String email,
            @RequestParam("otp") String otp,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("email", email);
            model.addAttribute("error", "Mật khẩu xác nhận không khớp.");
            return "auth/reset-password";
        }

        if (!otpService.verifyOtp(email, otp)) {
            model.addAttribute("email", email);
            model.addAttribute("error", "Mã OTP không đúng hoặc đã hết hạn.");
            return "auth/reset-password";
        }

        userService.updatePassword(email, newPassword);
        model.addAttribute("message", "Đổi mật khẩu thành công!");
        return "auth/login";
    }


    @PostMapping("/resend-otp")
    public String resendOtp(@RequestParam("email") String email, Model model) {
        if (!userService.existsByEmail(email)) {
            model.addAttribute("error", "Email chưa được đăng ký.");
            return "auth/forgot-password";
        }

        otpService.generateAndSendOtp(email);
        model.addAttribute("email", email);
        model.addAttribute("message", "Mã OTP mới đã được gửi đến email.");
        return "auth/reset-password";
    }

}

