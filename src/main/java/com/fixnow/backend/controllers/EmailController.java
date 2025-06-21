package com.fixnow.backend.controllers;


import com.fixnow.backend.dtos.request.EmailRequest;
import com.fixnow.backend.dtos.response.EmailResponse;
import com.fixnow.backend.services.EmailGenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final EmailGenerationService emailGenerationService;

    public EmailController(EmailGenerationService emailGenerationService) {
        this.emailGenerationService = emailGenerationService;
    }

    @PostMapping("/email_writing")
    public ResponseEntity<EmailResponse> generateMail(@RequestBody EmailRequest req) {
        return ResponseEntity.ok(emailGenerationService.generateEmail(req));
    }

}
