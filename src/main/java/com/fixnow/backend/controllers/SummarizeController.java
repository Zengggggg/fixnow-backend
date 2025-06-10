package com.fixnow.backend.controllers;

import com.fixnow.backend.dtos.request.SummarizeRequest;
import com.fixnow.backend.dtos.response.SummarizeResponse;
import com.fixnow.backend.services.SummarizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class SummarizeController {

    private final SummarizeService summarizeService;



    @PostMapping("/summarize")
    public ResponseEntity<SummarizeResponse> summarize(@RequestBody SummarizeRequest request) {
        // Nếu format là bullet, không dùng length
        String result = summarizeService.summarize(request.getText(),request.getLength(),request.getFormat());
        return ResponseEntity.ok(new SummarizeResponse(result));
    }
}
