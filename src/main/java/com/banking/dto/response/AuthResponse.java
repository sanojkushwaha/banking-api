package com.banking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO returned after successful login or registration.
 * Contains the JWT token and basic user info.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private String email;
    private String fullName;
    private String role;

    public AuthResponse(String token, String email, String fullName, String role) {
        this.token    = token;
        this.tokenType = "Bearer";
        this.email    = email;
        this.fullName = fullName;
        this.role     = role;
    }
}
