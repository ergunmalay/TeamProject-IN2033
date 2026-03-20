package com.novasolutions.ipospu.service;

import com.novasolutions.ipospu.db.MemberDAO;
import com.novasolutions.ipospu.model.Member;
import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;

// I put all membership business logic here, keeping the controllers and DAO thin.
public class MembershipService {

    private final MemberDAO memberDAO = new MemberDAO();

    // I define these character pools to satisfy the password complexity requirements in UC-01a.
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*";
    private static final String ALL = LETTERS + NUMBERS + SPECIAL;

    // I generate a random 10-character password that always contains at least
    // one letter, one number, and one special character, as required by UC-01a.
    String generatePassword() {
        SecureRandom random = new SecureRandom();
        char[] password = new char[10];

        // I guarantee one of each required type in the first three slots.
        password[0] = LETTERS.charAt(random.nextInt(LETTERS.length()));
        password[1] = NUMBERS.charAt(random.nextInt(NUMBERS.length()));
        password[2] = SPECIAL.charAt(random.nextInt(SPECIAL.length()));

        // I fill the remaining slots from the full character pool.
        for (int i = 3; i < 10; i++) {
            password[i] = ALL.charAt(random.nextInt(ALL.length()));
        }

        // I shuffle the array so the required characters don't always appear at the front.
        for (int i = 9; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = password[i];
            password[i] = password[j];
            password[j] = temp;
        }

        return new String(password);
    }

    /**
     * UC-02: Authenticates a member by verifying their email and BCrypt-hashed password.
     *
     * @param email the member's email address
     * @param password the plain-text password entered by the user
     * @return a LoginResult indicating success or failure; contains a user-facing
     * message describing the outcome
     */
    public LoginResult login(String email, String password) {
        // I reject null or blank inputs before touching the database.
        if (email == null || password == null) {
            System.out.println("❌ Email and password cannot be empty");
            return LoginResult.failure("Email and password cannot be empty.");
        }

        if (email.isBlank() || password.isBlank()) {
            System.out.println("❌ Email and password cannot be blank");
            return LoginResult.failure("Email and password cannot be blank.");
        }

        Member member = memberDAO.findByEmail(email);

        if (member == null) {
            System.out.println("❌ No member found with email: " + email);
            return LoginResult.failure("No account found with that email address.");
        }

        // I use BCrypt to compare the plain-text input against the stored hash.
        if (BCrypt.checkpw(password, member.passwordHash())) {
            System.out.println("✅ Login successful for " + member.fullName());
            return LoginResult.success("Login successful!");
        } else {
            System.out.println("❌ Invalid email or password");
            return LoginResult.failure("Invalid email or password.");
        }
    }

    /**
     * UC-01a: Registers a new non-commercial member.
     * Generates a random password, hashes it with BCrypt, and stores the account.
     * The is_first_login flag defaults to true in the database so the member can
     * be prompted to change their password on first login.
     *
     * @param name the member's full name
     * @param email the member's email address (used as their username)
     * @return a RegistrationResult indicating success or failure; contains the
     * generated password on success or an error message on failure
     */
    public RegistrationResult registerNonCommercial(String name, String email) {
        // I reject null or blank inputs early to avoid unnecessary DB calls.
        if (name == null || email == null) {
            System.out.println("❌ Name and email cannot be null");
            return RegistrationResult.failure("Name and email cannot be empty");
        }

        if (!email.contains("@")) {
            System.out.println("❌ Invalid email format: " + email);
            return RegistrationResult.failure("Invalid email format");
        }

        if (name.isBlank() || email.isBlank()) {
            System.out.println("❌ Name and email cannot be blank");
            return RegistrationResult.failure("Name and email cannot be blank");
        }

        // I check for duplicate emails before attempting to insert.
        if (memberDAO.emailExists(email)) {
            System.out.println("❌ Email already exists: " + email);
            return RegistrationResult.failure("Email already in use");
        }

        String plainPassword = generatePassword();
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        // I pass the full_name value and set the member type and status for non-commercial registration.
        boolean created = memberDAO.createMember(name, email, hashedPassword, "NON_COMMERCIAL", "APPROVED");
        if (created) {
            System.out.println("✅ Registration successful for " + name);
            return RegistrationResult.success(plainPassword);
        } else {
            System.out.println("❌ Registration failed for " + name);
            return RegistrationResult.failure("Registration failed due to a database error");
        }
    }
}