package com.banking.service.impl;

import com.banking.dto.request.TransactionRequest;
import com.banking.dto.response.AccountResponse;
import com.banking.dto.response.TransactionResponse;
import com.banking.entity.Account;
import com.banking.entity.Transaction;
import com.banking.entity.User;
import com.banking.enums.TransactionType;
import com.banking.exception.InsufficientFundsException;
import com.banking.exception.ResourceNotFoundException;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import com.banking.repository.UserRepository;
import com.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * AccountServiceImpl — concrete implementation of AccountService.
 *
 * Contains all the core banking business logic:
 *  deposit, withdrawal, transfer, transaction history.
 *
 * @Transactional ensures that if anything fails mid-operation,
 * the entire database change is rolled back (atomic operations).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final UserRepository        userRepository;
    private final AccountRepository     accountRepository;
    private final TransactionRepository transactionRepository;

    // -------------------------------------------------------
    //  Get Account Info
    // -------------------------------------------------------

    @Override
    public AccountResponse getMyAccount(String email) {
        Account account = getAccountByEmail(email);
        return mapToAccountResponse(account);
    }

    @Override
    public AccountResponse getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found with number: " + accountNumber));
        return mapToAccountResponse(account);
    }

    // -------------------------------------------------------
    //  Deposit
    // -------------------------------------------------------

    @Override
    @Transactional
    public TransactionResponse deposit(String email, TransactionRequest request) {
        log.info("Deposit request: {} by {}", request.getAmount(), email);

        Account account = getAccountByEmail(email);

        // Add money to balance
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        // Record transaction
        Transaction transaction = saveTransaction(
                account,
                TransactionType.DEPOSIT,
                request.getAmount(),
                null,
                request.getDescription() != null ? request.getDescription() : "Deposit",
                account.getBalance()
        );

        log.info("Deposit successful. New balance: {}", account.getBalance());
        return mapToTransactionResponse(transaction);
    }

    // -------------------------------------------------------
    //  Withdrawal
    // -------------------------------------------------------

    @Override
    @Transactional
    public TransactionResponse withdraw(String email, TransactionRequest request) {
        log.info("Withdrawal request: {} by {}", request.getAmount(), email);

        Account account = getAccountByEmail(email);

        // Check sufficient funds
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException(
                    "Insufficient funds. Available balance: " + account.getBalance());
        }

        // Deduct money from balance
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        // Record transaction
        Transaction transaction = saveTransaction(
                account,
                TransactionType.WITHDRAWAL,
                request.getAmount(),
                null,
                request.getDescription() != null ? request.getDescription() : "Withdrawal",
                account.getBalance()
        );

        log.info("Withdrawal successful. New balance: {}", account.getBalance());
        return mapToTransactionResponse(transaction);
    }

    // -------------------------------------------------------
    //  Transfer
    // -------------------------------------------------------

    @Override
    @Transactional
    public TransactionResponse transfer(String email, TransactionRequest request) {
        log.info("Transfer request: {} from {} to {}",
                request.getAmount(), email, request.getTargetAccountNumber());

        if (request.getTargetAccountNumber() == null || request.getTargetAccountNumber().isBlank()) {
            throw new IllegalArgumentException("Target account number is required for transfer");
        }

        Account senderAccount = getAccountByEmail(email);

        // Prevent transfer to own account
        if (senderAccount.getAccountNumber().equals(request.getTargetAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer money to your own account");
        }

        // Validate target account exists
        Account receiverAccount = accountRepository
                .findByAccountNumber(request.getTargetAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Target account not found: " + request.getTargetAccountNumber()));

        // Check sufficient funds in sender's account
        if (senderAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException(
                    "Insufficient funds. Available balance: " + senderAccount.getBalance());
        }

        // Deduct from sender
        senderAccount.setBalance(senderAccount.getBalance().subtract(request.getAmount()));
        accountRepository.save(senderAccount);

        // Add to receiver
        receiverAccount.setBalance(receiverAccount.getBalance().add(request.getAmount()));
        accountRepository.save(receiverAccount);

        // Record transaction on sender's account
        String desc = request.getDescription() != null
                ? request.getDescription()
                : "Transfer to " + receiverAccount.getAccountNumber();

        Transaction transaction = saveTransaction(
                senderAccount,
                TransactionType.TRANSFER,
                request.getAmount(),
                receiverAccount.getAccountNumber(),
                desc,
                senderAccount.getBalance()
        );

        log.info("Transfer successful. Sender balance: {}, Receiver balance: {}",
                senderAccount.getBalance(), receiverAccount.getBalance());

        return mapToTransactionResponse(transaction);
    }

    // -------------------------------------------------------
    //  Transaction History (Paginated)
    // -------------------------------------------------------

    @Override
    public Page<TransactionResponse> getTransactionHistory(String email, int page, int size) {
        Account account = getAccountByEmail(email);

        // Latest transactions first
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return transactionRepository
                .findByAccount(account, pageable)
                .map(this::mapToTransactionResponse);
    }

    // -------------------------------------------------------
    //  Private Helper Methods
    // -------------------------------------------------------

    /** Load account using the user's email. */
    private Account getAccountByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        return accountRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found for user: " + email));
    }

    /** Save a transaction record to the database. */
    private Transaction saveTransaction(
            Account account,
            TransactionType type,
            BigDecimal amount,
            String targetAccountNumber,
            String description,
            BigDecimal balanceAfter) {

        Transaction transaction = Transaction.builder()
                .account(account)
                .type(type)
                .amount(amount)
                .targetAccountNumber(targetAccountNumber)
                .description(description)
                .balanceAfterTransaction(balanceAfter)
                .build();

        return transactionRepository.save(transaction);
    }

    /** Convert Account entity → AccountResponse DTO. */
    private AccountResponse mapToAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .ownerName(account.getUser().getFullName())
                .ownerEmail(account.getUser().getEmail())
                .createdAt(account.getCreatedAt())
                .build();
    }

    /** Convert Transaction entity → TransactionResponse DTO. */
    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .targetAccountNumber(transaction.getTargetAccountNumber())
                .balanceAfterTransaction(transaction.getBalanceAfterTransaction())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
