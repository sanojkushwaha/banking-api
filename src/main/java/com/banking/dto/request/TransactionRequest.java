package com.banking.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO for deposit, withdrawal, and transfer requests.
 *
 * For DEPOSIT / WITHDRAWAL → only 'amount' is needed.
 * For TRANSFER             → both 'amount' and 'targetAccountNumber' are needed.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    /** Required only for TRANSFER transactions. */
    private String targetAccountNumber;

    /** Optional description/note for the transaction. */
    private String description;
}
