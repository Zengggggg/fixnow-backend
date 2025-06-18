package com.fixnow.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FeaturesController {
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
}
