package com.fixnow.backend.controllers;

import com.fixnow.backend.dtos.response.DiscountResponse;
import com.fixnow.backend.models.DiscountCode;
import com.fixnow.backend.repositories.DiscountCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/discount")
public class DiscountController {

    @Autowired
    private DiscountCodeRepository discountCodeRepository;

    @GetMapping("/validate")
    public ResponseEntity<?> validateDiscountCode(@RequestParam String code) {
        Optional<DiscountCode> optional = discountCodeRepository.findByCodeIgnoreCase(code.trim());

        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Mã giảm giá không tồn tại"));
        }

        DiscountCode discount = optional.get();

        if (!discount.isActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Mã giảm giá đã bị vô hiệu hóa"));
        }

        if (discount.getExpiryDate() != null && discount.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Mã giảm giá đã hết hạn"));
        }

        return ResponseEntity.ok(new DiscountResponse(discount.getCode(), discount.getPercent()));
    }
}
