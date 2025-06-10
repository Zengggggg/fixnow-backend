package com.fixnow.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FeaturesController {
    @GetMapping("/paraphraser")
    public String paraphraserPage() {
        return "features/paraphraser"; // paraphraser.jsp hoặc paraphraser.html tuỳ engine
    }

    @GetMapping("/grammar_checker")
    public String grammarCheckerPage() {
        return "features/grammar_checker";
    }

    @GetMapping("/summarizer")
    public String summarizerPage() {
        return "features/summarizer";
    }

    @GetMapping("/translate")
    public String translatePage() {
        return "features/translate";
    }
}
