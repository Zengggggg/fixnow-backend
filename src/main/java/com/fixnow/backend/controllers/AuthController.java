package com.fixnow.backend.controllers;

import com.fixnow.backend.dtos.request.ChangePasswordDto;
import com.fixnow.backend.dtos.request.LoginRequestDto;
import com.fixnow.backend.dtos.request.UserRegistrationDto;
import com.fixnow.backend.dtos.request.UserUpdateDto;
import com.fixnow.backend.models.User;
import com.fixnow.backend.models.UserWallet;
import com.fixnow.backend.models.VerificationToken;
import com.fixnow.backend.repositories.UserRepository;
import com.fixnow.backend.repositories.UserWalletRepository;
import com.fixnow.backend.repositories.VerificationTokenRepository;
import com.fixnow.backend.services.EmailVerifyService;
import com.fixnow.backend.services.GoogleAuthService;
import com.fixnow.backend.services.UserService;
import com.fixnow.backend.util.JwtUtil;
import com.fixnow.backend.dtos.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final EmailVerifyService emailVerifyService;
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final GoogleAuthService googleAuthService;
    private final UserWalletRepository userWalletRepository;

//    @Value("${google.client-id}")
    private String clientId = "424612164457-pmskbghvkdihh8lcs63odsb17bhukfs5.apps.googleusercontent.com";

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping("/doRegister")
    public String registerUser(@ModelAttribute UserRegistrationDto registrationDto,
                               RedirectAttributes redirectAttributes,
                               HttpServletRequest request) {
        try {
            userService.registerUser(registrationDto, request);

            log.info("User {} registered successfully. Verification email sent.", registrationDto.getUsername());

            return "redirect:/verify-notice?email=" + URLEncoder.encode(registrationDto.getEmail(), StandardCharsets.UTF_8);

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        } catch (Exception e) {
            log.error("Unexpected error during registration", e);
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token,
                                RedirectAttributes redirectAttributes) {
        Optional<VerificationToken> optional = tokenRepository.findByToken(token);

        if (optional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Token không hợp lệ hoặc đã hết hạn.");
            return "redirect:/login";
        }

        VerificationToken vt = optional.get();

        if (vt.getExpiryDate().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "Token đã hết hạn.");
            return "redirect:/login";
        }

        User user = vt.getUser();
        if (user.getWallet() == null) {
            UserWallet wallet = new UserWallet();
            wallet.setBalance(0.0); // hoặc giá trị mặc định của bạn
            wallet.setWordQuota(0);
            wallet.setUser(user);
            userWalletRepository.save(wallet);
            user.setWallet(wallet);
            user.setEnabled(true);

            userRepository.save(user);
        }


        tokenRepository.delete(vt);

        log.info("User {} verified successfully.", user.getUsername());
        redirectAttributes.addFlashAttribute("success", "Tài khoản đã được xác minh! Bạn có thể đăng nhập.");
        return "redirect:/login";
    }

    @GetMapping("/verify-notice")
    public String showVerifyNoticePage(Model model, @ModelAttribute("email") String email) {
        model.addAttribute("email", email);
        return "auth/verify-notice";
    }

    @PostMapping("/resend-verification")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> resendVerificationEmail(@RequestParam("email") String email,
                                                     HttpServletRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Không tìm thấy người dùng."));
        }

        User user = optionalUser.get();
        if (user.isEnabled()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Tài khoản đã được kích hoạt."));
        }

        // Xóa token cũ nếu có
        tokenRepository.deleteByUser(user);

        // Tạo token mới
        String token = UUID.randomUUID().toString();
        VerificationToken newToken = new VerificationToken();
        newToken.setToken(token);
        newToken.setUser(user);
        newToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        tokenRepository.save(newToken);

        emailVerifyService.sendVerificationEmail(user, token, request);

        log.info("Resent verification email to {}", email);
        return ResponseEntity.ok(Map.of("message", "Email xác minh đã được gửi lại."));
    }



    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequestDto());
        return "auth/login"; // maps to /WEB-INF/views/auth/login.html
    }

    @PatchMapping("/update")
    public ResponseEntity<?> patchUser(@RequestBody UserUpdateDto request, Principal principal) {
        String currentUsername = principal.getName(); // lấy từ người đang đăng nhập

        boolean success = userService.patchField(currentUsername, request.getField(), request.getValue());
        if (success) {
            return ResponseEntity.ok().body(Map.of("message", "Cập nhật thành công"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Trường không hợp lệ hoặc lỗi dữ liệu"));
        }
    }
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto dto, Principal principal) {
        String username = principal.getName();
        userService.changePassword(username, dto);
        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }

    @PostMapping("/google")
    public String authenticateWithGoogle(@RequestParam("token") String token,
                                         HttpSession session,
                                         RedirectAttributes redirectAttributes) {
        try {
            AuthenticationResponse response = googleAuthService.authenticate(token);
            session.setAttribute("jwtToken", response.getAccessToken());
            return "redirect:/home"; // or whatever page you want
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Google authentication failed.");
            return "redirect:/login";
        }
    }
} 