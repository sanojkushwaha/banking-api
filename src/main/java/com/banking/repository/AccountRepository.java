package com.banking.repository;

import com.banking.entity.Account;
import com.banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * AccountRepository — Data access layer for Account entity.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /** Find account by unique account number. */
    Optional<Account> findByAccountNumber(String accountNumber);

    /** Find account belonging to a specific user. */
    Optional<Account> findByUser(User user);

    /** Check if an account number already exists. */
    boolean existsByAccountNumber(String accountNumber);
}
