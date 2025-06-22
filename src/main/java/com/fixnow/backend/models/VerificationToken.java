package com.fixnow.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data

public class VerificationToken {

    @Id @GeneratedValue
    private Long id;

    private String token;

    @OneToOne
    private User user;

    private LocalDateTime expiryDate;


}
