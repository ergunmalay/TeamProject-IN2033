package com.novasolutions.ipospu.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MembershipServiceTest {

    // Create one instance of the service to use in tests
    MembershipService membershipService = new MembershipService();

    /**
     * Test: valid email and correct password
     * Expected: login succeeds (true)
     */
    @Test
    void login_validCredentials_returnsTrue() {
        // Arrange setup input
        String email = "test@example.com";
        String password = "password123";

        // Act call method
        boolean result = membershipService.login(email, password);

        // Assert check expected result
        assertTrue(result);
    }

    /**
     * Test: valid email but wrong password
     * Expected: login fails (false)
     */
    @Test
    void login_wrongPassword_returnsFalse() {
        String email = "test@example.com";
        String password = "wrongpassword";

        boolean result = membershipService.login(email, password);

        assertFalse(result);
    }

    /**
     * Test: email does not exist in database
     * Expected: login fails (false)
     */
    @Test
    void login_unknownEmail_returnsFalse() {
        String email = "fake@example.com";
        String password = "password123";

        boolean result = membershipService.login(email, password);

        assertFalse(result);
    }

    /**
     * Test: blank email
     * Expected: login fails (false)
     */
    @Test
    void login_blankEmail_returnsFalse() {
        String email = "";
        String password = "password123";

        boolean result = membershipService.login(email, password);

        assertFalse(result);
    }

    /**
     * Test: null password
     * Expected: login fails (false)
     */
    @Test
    void login_nullPassword_returnsFalse() {
        String email = "test@example.com";
        String password = null;

        boolean result = membershipService.login(email, password);

        assertFalse(result);
    }
}