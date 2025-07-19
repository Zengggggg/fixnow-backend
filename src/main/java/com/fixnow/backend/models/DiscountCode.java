package com.fixnow.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCode {
    @Id
    private String code;

    private int percent; // ví dụ: 10, 20

    private boolean active;

    private LocalDateTime expiryDate;

    // Getters & Setters
}
