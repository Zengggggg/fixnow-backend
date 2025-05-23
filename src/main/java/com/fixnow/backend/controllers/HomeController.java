package com.fixnow.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    public String showHomePage() {
        return "home/home"; // This will resolve to /WEB-INF/views/home.jsp
    }
}