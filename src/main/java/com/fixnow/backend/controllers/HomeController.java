package com.fixnow.backend.controllers;

import com.fixnow.backend.models.User;
import com.fixnow.backend.repositories.UserRepository;
import com.fixnow.backend.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {
    UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showHomePage(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            Optional<User> userOpt = userRepository.findByEmailWithRole(email);
            userOpt.ifPresent(user -> model.addAttribute("user", user));
        }
        return "home/home"; // trang home vẫn hiển thị bình thường
    }
}