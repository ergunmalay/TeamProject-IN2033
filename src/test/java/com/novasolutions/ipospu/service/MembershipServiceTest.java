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
        RegistrationResult result = membershipService.registerNonCommercial("Test User", testEmail);
        assertTrue(result.isSuccess(), "Test setup failed: could not register test member");
        testPassword = result.getPassword();
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
        assertTrue(membershipService.login(testEmail, testPassword).isSuccess());
    }

    /**
     * Valid email but wrong password → login fails.
     */
    @Test
    void login_wrongPassword_returnsFalse() {
        assertFalse(membershipService.login(testEmail, "wrongpassword").isSuccess());
    }

    /**
     * Email not registered in the system → login fails.
     */
    @Test
    void login_unknownEmail_returnsFalse() {
        assertFalse(membershipService.login("nobody@example.com", testPassword).isSuccess());
    }

    /**
     * Blank email → login fails.
     */
    @Test
    void login_blankEmail_returnsFalse() {
        assertFalse(membershipService.login("", testPassword).isSuccess());
    }

    /**
     * Null password → login fails.
     */
    @Test
    void login_nullPassword_returnsFalse() {
        assertFalse(membershipService.login(testEmail, null).isSuccess());
    }

    // ─── Registration tests (UC-01a) ─────────────────────────────────────────

    /**
     * Valid name and unused email → registration succeeds and returns a generated password.
     */
    @Test
    void registration_validInput_returnsGeneratedPassword() {
        String email = "newuser_" + System.currentTimeMillis() + "@example.com";
        RegistrationResult result = membershipService.registerNonCommercial("New User", email);

        assertTrue(result.isSuccess());
        assertNotNull(result.getPassword());
        assertEquals(10, result.getPassword().length());

        // Cleanup extra member created by this test
        memberDAO.deleteMemberByEmail(email);
    }

    /**
     * Email already registered → registration fails.
     */
    @Test
    void registration_duplicateEmail_returnsFailure() {
        RegistrationResult result = membershipService.registerNonCommercial("Test User", testEmail);
        assertFalse(result.isSuccess());
        assertNotNull(result.getMessage());
    }

    /**
     * Blank name → registration fails.
     */
    @Test
    void registration_blankName_returnsFailure() {
        String email = "blankname_" + System.currentTimeMillis() + "@example.com";
        RegistrationResult result = membershipService.registerNonCommercial("", email);
        assertFalse(result.isSuccess());
    }

    /**
     * Null email → registration fails.
     */
    @Test
    void registration_nullEmail_returnsFailure() {
        RegistrationResult result = membershipService.registerNonCommercial("Test User", null);
        assertFalse(result.isSuccess());
    }

    /**
     * Blank email → registration fails.
     */
    @Test
    void registration_blankEmail_returnsFailure() {
        RegistrationResult result = membershipService.registerNonCommercial("Test User", "");
        assertFalse(result.isSuccess());
    }

    // ─── Email @ symbol validation tests ─────────────────────────────────────

    /**
     * Email with no @ symbol → registration fails with invalid format message.
     */
    @Test
    void registration_emailWithoutAtSymbol_returnsFailure() {
        RegistrationResult result = membershipService.registerNonCommercial("Test User", "notanemail.com");
        assertFalse(result.isSuccess());
        assertEquals("Invalid email format", result.getMessage());
    }

    /**
     * Email that is just a word with no @ or domain → registration fails.
     */
    @Test
    void registration_emailPlainWord_returnsFailure() {
        RegistrationResult result = membershipService.registerNonCommercial("Test User", "noatsymbol");
        assertFalse(result.isSuccess());
        assertEquals("Invalid email format", result.getMessage());
    }

    /**
     * Email with @ present → passes the format check and proceeds normally.
     */
    @Test
    void registration_emailWithAtSymbol_passesFormatCheck() {
        String email = "valid_" + System.currentTimeMillis() + "@domain.com";
        RegistrationResult result = membershipService.registerNonCommercial("Test User", email);
        assertTrue(result.isSuccess());
        memberDAO.deleteMemberByEmail(email);
    }

    /**
     * Login with an email missing @ → should fail (no account with that address).
     */
    @Test
    void login_emailWithoutAtSymbol_returnsFalse() {
        assertFalse(membershipService.login("notanemail", testPassword).isSuccess());
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
