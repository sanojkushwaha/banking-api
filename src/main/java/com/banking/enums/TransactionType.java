package com.banking.enums;

/**
 * Types of banking transactions supported by the system.
 *
 * DEPOSIT    → Money added to an account.
 * WITHDRAWAL → Money taken out from an account.
 * TRANSFER   → Money moved from one account to another.
 */
public enum TransactionType {
    DEPOSIT,
    WITHDRAWAL,
    TRANSFER
}
