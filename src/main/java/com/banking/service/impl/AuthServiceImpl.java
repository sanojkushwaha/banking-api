package com.banking.service.impl;

import com.banking.dto.request.LoginRequest;
import com.banking.dto.request.RegisterRequest;
import com.banking.dto.response.AuthResponse;
import com.banking.entity.Account;
import com.banking.entity.User;
import com.banking.enums.Role;
import com.banking.exception.DuplicateResourceException;
import com.banking.repository.AccountRepository;
import com.banking.repository.UserRepository;
import com.banking.security.service.JwtService;
import com.banking.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;

/**
 * AuthServiceImpl — concrete implementation of AuthService.
 *
 * Handles user registration and login logic.
 */
@Service
@RequiredArgsConstructor
@Slf4j  // enables log.info(), log.error(), etc.
public class AuthServiceImpl implements AuthService {

    private final UserRepository         userRepository;
    private final AccountRepository      accountRepository;
    private final PasswordEncoder        passwordEncoder;
    private final JwtService             jwtService;
    private final AuthenticationManager  authManager;

    // -------------------------------------------------------
    //  Register
    // -------------------------------------------------------

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Check for duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Email already registered: " + request.getEmail());
        }

        // Check for duplicate phone number
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateResourceException(
                    "Phone number already registered: " + request.getPhoneNumber());
        }

        // Create and save the User
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.ROLE_USER)
                .build();

        user = userRepository.save(user);
        log.info("User saved with ID: {}", user.getId());

        // Auto-create a bank account for the new user
        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();

        accountRepository.save(account);
        log.info("Account created: {}", account.getAccountNumber());

        // Generate JWT token
        String token = jwtService.generateToken(user);

        return new AuthResponse(token, user.getEmail(), user.getFullName(), user.getRole().name());
    }

    // -------------------------------------------------------
    //  Login
    // -------------------------------------------------------

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        // AuthenticationManager verifies email + password
        // Throws BadCredentialsException if wrong → caught by GlobalExceptionHandler
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // If authentication passed, load the user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);
        log.info("Login successful for: {}", user.getEmail());

        return new AuthResponse(token, user.getEmail(), user.getFullName(), user.getRole().name());
    }

    // -------------------------------------------------------
    //  Helper: Generate unique 10-digit account number
    // -------------------------------------------------------

    private String generateAccountNumber() {
        Random random = new Random();
        String accountNumber;

        // Keep generating until we get a unique number
        do {
            long number = 1000000000L + (long)(random.nextDouble() * 9000000000L);
            accountNumber = String.valueOf(number);
        } while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }
}
