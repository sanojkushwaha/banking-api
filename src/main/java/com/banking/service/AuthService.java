package com.banking.service;

import com.banking.dto.request.LoginRequest;
import com.banking.dto.request.RegisterRequest;
import com.banking.dto.response.AuthResponse;

/**
 * AuthService — defines the contract for authentication operations.
 *
 * Interface → Implementation pattern is used so that the code
 * depends on abstraction, not concrete classes (SOLID principle).
 */
public interface AuthService {

    /**
     * Registers a new user and creates their bank account.
     * @param request contains name, email, password, phone number
     * @return JWT token + user info
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticates an existing user.
     * @param request contains email and password
     * @return JWT token + user info
     */
    AuthResponse login(LoginRequest request);
}
