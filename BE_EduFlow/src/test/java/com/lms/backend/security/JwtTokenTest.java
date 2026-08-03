package com.lms.backend.security;

import com.lms.backend.model.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenTest {

    @Test
    void stableInfrastructureSecretGeneratesValidTokensAcrossInstances() {
        CustomUserDetails user = userDetails();
        JwtToken issuer = tokenService("stable-infrastructure-secret");
        JwtToken verifier = tokenService("stable-infrastructure-secret");

        String token = issuer.generateToken(Map.of("role", "STUDENT"), user);

        assertEquals("student@example.com", verifier.extractUsername(token));
        assertTrue(verifier.isTokenValid(token, user));
    }

    @Test
    void missingSigningSecretFailsAtStartupInsteadOfDuringLogin() {
        JwtToken token = new JwtToken();
        ReflectionTestUtils.setField(token, "secretKey", " ");

        assertThrows(IllegalStateException.class, token::initializeSigningKey);
    }

    private JwtToken tokenService(String secret) {
        JwtToken token = new JwtToken();
        ReflectionTestUtils.setField(token, "secretKey", secret);
        token.initializeSigningKey();
        return token;
    }

    private CustomUserDetails userDetails() {
        Account account = new Account();
        account.setUsername("student@example.com");
        account.setPassword("encoded");
        account.setRole("STUDENT");
        account.setStatus(true);
        return new CustomUserDetails(account);
    }
}
