package com.fixnow.backend.controllers;


import com.fixnow.backend.dtos.request.TextRequest;
import com.fixnow.backend.dtos.response.GrammarError;
import com.fixnow.backend.services.GrammarCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GrammarCheckController {

    private final GrammarCheckService grammarCheckService;

    @PostMapping("/grammar_check")
    public ResponseEntity<?> checkGrammar(@RequestBody TextRequest request) {
        List<GrammarError> errors = grammarCheckService.checkText(request.getText());
        return ResponseEntity.ok(Map.of("errors", errors));
    }
}
