package com.banking.controller;

import com.banking.dto.request.TransactionRequest;
import com.banking.dto.response.AccountResponse;
import com.banking.dto.response.ApiResponse;
import com.banking.dto.response.TransactionResponse;
import com.banking.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * AccountController — handles all account and transaction operations.
 *
 * All endpoints require a valid JWT token (Authorization: Bearer <token>).
 *
 * @AuthenticationPrincipal UserDetails → Spring injects the logged-in user automatically.
 * We extract the email from it to identify which user is making the request.
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")   // show lock icon in Swagger UI
@Tag(name = "Account & Transactions", description = "Manage accounts and perform transactions")
public class AccountController {

    private final AccountService accountService;

    // -------------------------------------------------------
    //  Account Endpoints
    // -------------------------------------------------------

    /**
     * GET /api/accounts/me
     * Get the currently logged-in user's account details.
     */
    @GetMapping("/me")
    @Operation(summary = "Get my account", description = "Returns the bank account details of the logged-in user.")
    public ResponseEntity<ApiResponse<AccountResponse>> getMyAccount(
            @AuthenticationPrincipal UserDetails userDetails) {

        AccountResponse account = accountService.getMyAccount(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Account retrieved successfully", account));
    }

    /**
     * GET /api/accounts/{accountNumber}
     * Get account by account number. ADMIN ONLY.
     */
    @GetMapping("/{accountNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "[ADMIN] Get account by number", description = "Admin only. Retrieve any account by its account number.")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccountByNumber(
            @PathVariable String accountNumber) {

        AccountResponse account = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(ApiResponse.success("Account retrieved successfully", account));
    }

    // -------------------------------------------------------
    //  Transaction Endpoints
    // -------------------------------------------------------

    /**
     * POST /api/accounts/deposit
     * Deposit money into logged-in user's account.
     */
    @PostMapping("/deposit")
    @Operation(summary = "Deposit money", description = "Add money to your account. Requires 'amount' in request body.")
    public ResponseEntity<ApiResponse<TransactionResponse>> deposit(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TransactionRequest request) {

        TransactionResponse transaction = accountService.deposit(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success(
                "Deposit of " + request.getAmount() + " successful!", transaction));
    }

    /**
     * POST /api/accounts/withdraw
     * Withdraw money from logged-in user's account.
     */
    @PostMapping("/withdraw")
    @Operation(summary = "Withdraw money", description = "Withdraw money from your account. Must have sufficient balance.")
    public ResponseEntity<ApiResponse<TransactionResponse>> withdraw(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TransactionRequest request) {

        TransactionResponse transaction = accountService.withdraw(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success(
                "Withdrawal of " + request.getAmount() + " successful!", transaction));
    }

    /**
     * POST /api/accounts/transfer
     * Transfer money to another account.
     */
    @PostMapping("/transfer")
    @Operation(summary = "Transfer money", description = "Transfer money to another account. Requires 'amount' and 'targetAccountNumber'.")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TransactionRequest request) {

        TransactionResponse transaction = accountService.transfer(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success(
                "Transfer of " + request.getAmount() + " successful!", transaction));
    }

    /**
     * GET /api/accounts/transactions?page=0&size=10
     * Get paginated transaction history for the logged-in user.
     */
    @GetMapping("/transactions")
    @Operation(summary = "Transaction history", description = "Get paginated transaction history. Use ?page=0&size=10")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getTransactions(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<TransactionResponse> transactions =
                accountService.getTransactionHistory(userDetails.getUsername(), page, size);

        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactions));
    }
}
