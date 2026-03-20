package com.novasolutions.ipospu.service;

import com.novasolutions.ipospu.db.MemberDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MembershipServiceTest {

    private final MembershipService membershipService = new MembershipService();
    private final MemberDAO memberDAO = new MemberDAO();

    private String testEmail;
    private String testPassword;

    /**
     * Before each test, register a fresh non-commercial member so login tests
     * have a real BCrypt-hashed account to work against.
     */
    @BeforeEach
    void setUp() {
        testEmail = "testuser_" + System.currentTimeMillis() + "@example.com";
        testPassword = membershipService.registerNonCommercial("Test User", testEmail);
        assertNotNull(testPassword, "Test setup failed: could not register test member");
    }

    @AfterEach
    void cleanup() {
        memberDAO.deleteMemberByEmail(testEmail);
    }

    // ─── Login tests (UC-02) ─────────────────────────────────────────────────

    /**
     * Valid email and generated password → login succeeds.
     */
    @Test
    void login_validCredentials_returnsTrue() {
        assertTrue(membershipService.login(testEmail, testPassword));
    }

    /**
     * Valid email but wrong password → login fails.
     */
    @Test
    void login_wrongPassword_returnsFalse() {
        assertFalse(membershipService.login(testEmail, "wrongpassword"));
    }

    /**
     * Email not registered in the system → login fails.
     */
    @Test
    void login_unknownEmail_returnsFalse() {
        assertFalse(membershipService.login("nobody@example.com", testPassword));
    }

    /**
     * Blank email → login fails.
     */
    @Test
    void login_blankEmail_returnsFalse() {
        assertFalse(membershipService.login("", testPassword));
    }

    /**
     * Null password → login fails.
     */
    @Test
    void login_nullPassword_returnsFalse() {
        assertFalse(membershipService.login(testEmail, null));
    }

    // ─── Registration tests (UC-01a) ─────────────────────────────────────────

    /**
     * Valid name and unused email → registration succeeds and returns a generated password.
     */
    @Test
    void registration_validInput_returnsGeneratedPassword() {
        String email = "newuser_" + System.currentTimeMillis() + "@example.com";
        String password = membershipService.registerNonCommercial("New User", email);

        assertNotNull(password);
        assertEquals(10, password.length());

        // Cleanup extra member created by this test
        memberDAO.deleteMemberByEmail(email);
    }

    /**
     * Email already registered → registration fails (returns null).
     */
    @Test
    void registration_duplicateEmail_returnsNull() {
        assertNull(membershipService.registerNonCommercial("Test User", testEmail));
    }

    /**
     * Blank name → registration fails.
     */
    @Test
    void registration_blankName_returnsNull() {
        String email = "blankname_" + System.currentTimeMillis() + "@example.com";
        assertNull(membershipService.registerNonCommercial("", email));
    }

    /**
     * Null email → registration fails.
     */
    @Test
    void registration_nullEmail_returnsNull() {
        assertNull(membershipService.registerNonCommercial("Test User", null));
    }

    /**
     * Blank email → registration fails.
     */
    @Test
    void registration_blankEmail_returnsNull() {
        assertNull(membershipService.registerNonCommercial("Test User", ""));
    }

    // ─── Password generation tests ────────────────────────────────────────────

    /**
     * Generated password must be exactly 10 characters.
     */
    @Test
    void generatePassword_isExactlyTenCharacters() {
        assertEquals(10, membershipService.generatePassword().length());
    }

    /**
     * Generated password must contain at least one letter.
     */
    @Test
    void generatePassword_containsLetter() {
        assertTrue(membershipService.generatePassword().chars().anyMatch(Character::isLetter));
    }

    /**
     * Generated password must contain at least one digit.
     */
    @Test
    void generatePassword_containsDigit() {
        assertTrue(membershipService.generatePassword().chars().anyMatch(Character::isDigit));
    }

    /**
     * Generated password must contain at least one special character.
     */
    @Test
    void generatePassword_containsSpecialCharacter() {
        String special = "!@#$%^&*";
        assertTrue(membershipService.generatePassword().chars()
                .anyMatch(c -> special.indexOf(c) >= 0));
    }
}