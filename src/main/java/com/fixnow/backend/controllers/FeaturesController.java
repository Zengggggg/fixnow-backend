package com.fixnow.backend.controllers;

import com.fixnow.backend.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FeaturesController {

    private final UserRepository userRepository;

    public FeaturesController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/paraphraser")
    public String paraphraserPage() {
        return "features/paraphraser"; // maps to /WEB-INF/views/features/paraphraser.html
    }

    @GetMapping("/grammar_checker")
    public String grammarCheckerPage() {
        return "features/grammar_checker"; // maps to /WEB-INF/views/features/grammar_checker.html
    }

    @GetMapping("/summarizer")
    public String summarizerPage() {
        return "features/summarizer"; // maps to /WEB-INF/views/features/summarizer.html
    }

    @GetMapping("/translate")
    public String translatePage() {
        return "features/translate"; // maps to /WEB-INF/views/features/translate.html
    }

    @GetMapping("/under_development_process")
    public String underDevelopmentProcessPage() {
        return "features/under_development_process";
    }

    @GetMapping("/email_writing_assistant")
    public String emailWritingAssistantPage(Model model) {
        // Lấy email từ security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // đây là email, vì bạn dùng email làm username

        // Tìm user từ DB để lấy tên đầy đủ
        com.fixnow.backend.models.User user = userRepository.findByEmailWithRole(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String fullName = user.getFirstName() + " " + user.getLastName();
        int length = fullName.length();

        // Đưa tên người dùng vào model

        model.addAttribute("userName", fullName);
        model.addAttribute("userEmail", email);
        model.addAttribute("nameLength", length);
        return "features/email_assistant";
    }
}
