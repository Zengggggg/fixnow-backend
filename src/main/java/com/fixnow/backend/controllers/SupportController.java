package com.fixnow.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SupportController {

    @GetMapping("/term-and-service")
    public String termAndService() {
        return "support/term-of-service";
    }
    @GetMapping("/policy")
    public String policy() {
        return "support/policy";
    }

}
