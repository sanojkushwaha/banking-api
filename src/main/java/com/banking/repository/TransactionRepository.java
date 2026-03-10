package com.banking.repository;

import com.banking.entity.Account;
import com.banking.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * TransactionRepository — Data access layer for Transaction entity.
 *
 * Uses Spring Data JPA's Pageable for paginated transaction history.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Get all transactions for a given account, sorted with pagination.
     * Example usage: findByAccount(account, PageRequest.of(0, 10, Sort.by("createdAt").descending()))
     */
    Page<Transaction> findByAccount(Account account, Pageable pageable);
}
