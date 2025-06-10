package com.fixnow.backend.controllers;


import com.fixnow.backend.dtos.request.ParaphraseRequest;
import com.fixnow.backend.dtos.response.ParaphraseResponse;
import com.fixnow.backend.services.ParaphraseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class ParaphraseController {

    private final ParaphraseService paraphraseService;


    @PostMapping("/paraphrase")
    public ResponseEntity<ParaphraseResponse> paraphrase(@RequestBody ParaphraseRequest request) {
        String originalText = request.getText();
        String paraphrased = paraphraseService.paraphrase(originalText);

        ParaphraseResponse response = new ParaphraseResponse(paraphrased);
        return ResponseEntity.ok(response);
    }

}
