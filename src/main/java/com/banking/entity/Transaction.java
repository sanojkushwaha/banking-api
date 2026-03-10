package com.banking.entity;

import com.banking.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction Entity — records every financial operation.
 *
 * Every DEPOSIT, WITHDRAWAL, or TRANSFER creates a Transaction record.
 * For TRANSFER: the sender's account stores the transaction.
 *
 * Table: transactions
 */
@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Type of transaction: DEPOSIT, WITHDRAWAL, or TRANSFER. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    /** Amount involved in this transaction. */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    /** Account that initiated this transaction. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    /**
     * Target account number (only for TRANSFER transactions).
     * Null for DEPOSIT and WITHDRAWAL.
     */
    @Column
    private String targetAccountNumber;

    /** Short description or note for this transaction. */
    @Column
    private String description;

    /** Account balance AFTER this transaction was applied. */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balanceAfterTransaction;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
