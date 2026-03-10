package com.banking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Account Entity — represents a bank account owned by a User.
 *
 * One User can have one Account (OneToOne relationship).
 * One Account can have many Transactions (OneToMany relationship).
 *
 * Table: accounts
 */
@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique 10-digit account number.
     * Generated automatically when an account is created.
     */
    @Column(nullable = false, unique = true, length = 10)
    private String accountNumber;

    /**
     * Current balance of the account.
     * Uses BigDecimal for precise monetary calculations.
     * Default balance is 0.00
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    /** The user who owns this account (Foreign Key). */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** All transactions linked to this account. */
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.balance == null) {
            this.balance = BigDecimal.ZERO;
        }
    }
}
