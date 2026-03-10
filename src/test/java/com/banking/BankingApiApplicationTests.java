package com.banking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * BankingApiApplicationTests — verifies the application context loads correctly.
 *
 * This test checks that all beans, configurations, and dependencies
 * are wired correctly without starting a real HTTP server.
 */
@SpringBootTest
@ActiveProfiles("test")
class BankingApiApplicationTests {

    @Test
    void contextLoads() {
        // If this test passes, the Spring context loaded successfully.
        // All beans, security config, JPA, etc. are correctly configured.
        System.out.println("✅ Spring context loaded successfully!");
    }
}
