package com.fixnow.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "topup_requests")
@Getter
@Setter
public class TopUpRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private Double amount;

    private LocalDateTime requestTime;

    private boolean confirmed = false;

    private LocalDateTime confirmedTime;

    public TopUpRequest(User user, Double amount, LocalDateTime requestTime, boolean confirmed, LocalDateTime confirmedTime) {
        this.user = user;
        this.amount = amount;
        this.requestTime = requestTime;
        this.confirmed = confirmed;
        this.confirmedTime = confirmedTime;
    }

    public TopUpRequest() {
    }
}

