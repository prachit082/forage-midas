package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserRecord sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserRecord recipient;

    private float incentive;

    public TransactionRecord(double amount, UserRecord sender, UserRecord recipient, float incentive) {
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
        this.incentive = incentive;
        this.timestamp = LocalDateTime.now();
    }
}
