package com.banking.service;

import com.banking.dto.request.TransactionRequest;
import com.banking.dto.response.AccountResponse;
import com.banking.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;

/**
 * AccountService — defines the contract for all account operations.
 */
public interface AccountService {

    /** Get the account details for the currently logged-in user. */
    AccountResponse getMyAccount(String email);

    /** Get account details by account number (Admin only). */
    AccountResponse getAccountByNumber(String accountNumber);

    /** Deposit money into the currently logged-in user's account. */
    TransactionResponse deposit(String email, TransactionRequest request);

    /** Withdraw money from the currently logged-in user's account. */
    TransactionResponse withdraw(String email, TransactionRequest request);

    /** Transfer money to another account. */
    TransactionResponse transfer(String email, TransactionRequest request);

    /**
     * Get paginated transaction history for the logged-in user.
     * @param page  page number (starts at 0)
     * @param size  number of records per page
     */
    Page<TransactionResponse> getTransactionHistory(String email, int page, int size);
}
