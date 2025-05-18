package com.fixnow.backend.controllers;

import com.fixnow.backend.dtos.request.LoginRequestDto;
import com.fixnow.backend.dtos.request.UserRegistrationDto;
import com.fixnow.backend.dtos.request.UserUpdateDto;
import com.fixnow.backend.models.User;
import com.fixnow.backend.services.GoogleAuthService;
import com.fixnow.backend.services.UserService;
import com.fixnow.backend.util.JwtUtil;
import com.fixnow.backend.dtos.response.AuthenticationResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder; // Needed for login check
    private final JwtUtil jwtUtil; // Inject JwtUtil
    private final GoogleAuthService googleAuthService;

//    @Value("${google.client-id}")
    private String clientId = "424612164457-pmskbghvkdihh8lcs63odsb17bhukfs5.apps.googleusercontent.com";

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "auth/register"; // View name (e.g., register.html or register.jsp)
    }
    @PostMapping("/doRegister")
    public String registerUser(@ModelAttribute UserRegistrationDto registrationDto,
                               RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(registrationDto);
            redirectAttributes.addFlashAttribute("success", "Registration successful. Please login.");
            log.info("User {} registered successfully", registrationDto.getUsername());
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred."+e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequestDto());
        return "auth/login"; // maps to /WEB-INF/views/auth/login.jsp
    }

    @PostMapping("/doLogin")
    public String loginUser(@ModelAttribute LoginRequestDto loginRequestDto, Model model, HttpSession session) {
        try {
            User user = userService.findByEmail(loginRequestDto.getEmail());
            if (passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
                // Store user in session, then redirect to home
                session.setAttribute("user", user);
                log.info("User {} logged in successfully", user.getUsername());
                return "redirect:/home";
            } else {
                model.addAttribute("error", "Invalid email or password. Please try again.");
                return "auth/login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Login error occurred");
            return "auth/login";
        }
    }

    @GetMapping("/update")
    public String showUpdateForm(Model model, @RequestParam("id") Long id) {
        User user = userService.findById(id); // Assumes a method to fetch user
        model.addAttribute("user", user);
        return "update-user"; // View name (e.g., update-user.html)
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") UserUpdateDto updateDto,
                             RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(JwtUtil.getCurrentUserId(), updateDto);
            redirectAttributes.addFlashAttribute("success", "User updated successfully.");
            return "redirect:/home";
        } catch (jakarta.persistence.EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/update?id=" + JwtUtil.getCurrentUserId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/update?id=" + JwtUtil.getCurrentUserId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update user.");
            return "redirect:/update?id=" + JwtUtil.getCurrentUserId();
        }
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