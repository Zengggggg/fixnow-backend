package com.fixnow.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String type; // PURCHASE_WORD, USAGE, REFUND,...

    private Integer wordsChanged; // Có thể là âm (khi sử dụng)

    private Double amountChanged; // Có thể là âm (khi thanh toán)

    private String description;

    private LocalDateTime timestamp;
}
