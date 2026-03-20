package com.novasolutions.ipospu.service;

import com.novasolutions.ipospu.db.MemberDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MembershipServiceTest {

    private final MembershipService membershipService = new MembershipService();
    private final MemberDAO memberDAO = new MemberDAO();
    private String testEmail;

    /**
     * Test: valid email and correct password
     * Expected: login succeeds (true)
     */
    @Test
    void login_validCredentials_returnsTrue() {
        String email = "test@example.com";
        String password = "password123";

        boolean result = membershipService.login(email, password);

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

    /**
     * Test: valid new registration
     * Expected: registration succeeds (true)
     */
    @Test
    void registration_validCredentials_returnsTrue() {
        testEmail = "newuser_" + System.currentTimeMillis() + "@example.com";
        String password = "password123";

        boolean result = membershipService.registerNonCommercial("Test User", testEmail, password);

        assertTrue(result);
    }

    /**
     * Test: duplicate email registration
     * Expected: registration fails (false)
     */
    @Test
    void registration_duplicateEmail_returnsFalse() {
        String email = "test@example.com";
        String password = "password123";

        boolean result = membershipService.registerNonCommercial("Test User", email, password);

        assertFalse(result);
    }

    /**
     * Test: blank name
     * Expected: registration fails (false)
     */
    @Test
    void registration_blankName_returnsFalse() {
        String email = "blankname_" + System.currentTimeMillis() + "@example.com";
        String password = "password123";

        boolean result = membershipService.registerNonCommercial("", email, password);

        assertFalse(result);
    }

    /**
     * Test: null email
     * Expected: registration fails (false)
     */
    @Test
    void registration_nullEmail_returnsFalse() {
        String password = "password123";

        boolean result = membershipService.registerNonCommercial("Test User", null, password);

        assertFalse(result);
    }

    /**
     * Test: blank password
     * Expected: registration fails (false)
     */
    @Test
    void registration_blankPassword_returnsFalse() {
        String email = "blankpassword_" + System.currentTimeMillis() + "@example.com";

        boolean result = membershipService.registerNonCommercial("Test User", email, "");

        assertFalse(result);
    }

    @AfterEach
    void cleanup() {
        if (testEmail != null) {
            memberDAO.deleteMemberByEmail(testEmail);
            testEmail = null;
        }
    }
}