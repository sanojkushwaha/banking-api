package com.banking.repository;

import com.banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository — Data access layer for User entity.
 *
 * Spring Data JPA auto-generates all basic CRUD operations.
 * We only define custom query methods here.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /** Find a user by their email address (used for login). */
    Optional<User> findByEmail(String email);

    /** Check if an email is already registered. */
    boolean existsByEmail(String email);

    /** Check if a phone number is already registered. */
    boolean existsByPhoneNumber(String phoneNumber);
}
