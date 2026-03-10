package com.banking.enums;

/**
 * Defines the roles available in the system.
 *
 * ROLE_USER  → Regular bank customer. Can access only their own account.
 * ROLE_ADMIN → Bank admin. Can view all accounts and all transactions.
 */
public enum Role {
    ROLE_USER,
    ROLE_ADMIN
}
