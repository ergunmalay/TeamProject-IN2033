package com.novasolutions.ipospu.service;

import com.novasolutions.ipospu.db.MemberDAO;
import com.novasolutions.ipospu.model.Member;
import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;

public class MembershipService {

    private final MemberDAO memberDAO = new MemberDAO();

    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*";
    private static final String ALL = LETTERS + NUMBERS + SPECIAL;

    /**
     * Generates a random 10-character password containing at least one letter,
     * one number, and one special character, as required by UC-01a.
     */
    String generatePassword() {
        SecureRandom random = new SecureRandom();
        char[] password = new char[10];

        // Guarantee at least one of each required character type
        password[0] = LETTERS.charAt(random.nextInt(LETTERS.length()));
        password[1] = NUMBERS.charAt(random.nextInt(NUMBERS.length()));
        password[2] = SPECIAL.charAt(random.nextInt(SPECIAL.length()));

        for (int i = 3; i < 10; i++) {
            password[i] = ALL.charAt(random.nextInt(ALL.length()));
        }

        // Shuffle to avoid predictable positions
        for (int i = 9; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = password[i];
            password[i] = password[j];
            password[j] = temp;
        }

        return new String(password);
    }

    /**
     * UC-02: Authenticates a registered member using email and BCrypt password verification.
     *
     * @return true if credentials are valid, false otherwise
     */
    public boolean login(String email, String password) {
        if (email == null || password == null) {
            System.out.println("❌ Email and password cannot be empty");
            return false;
        }

        if (email.isBlank() || password.isBlank()) {
            System.out.println("❌ Email and password cannot be blank");
            return false;
        }

        Member member = memberDAO.findByEmail(email);

        if (member == null) {
            System.out.println("❌ No member found with email: " + email);
            return false;
        }

        if (BCrypt.checkpw(password, member.passwordHash())) {
            System.out.println("✅ Login successful for " + member.name());
            return true;
        } else {
            System.out.println("❌ Invalid email or password");
            return false;
        }
    }

    /**
     * UC-01a: Registers a new non-commercial member.
     * Generates a random 10-character password, hashes it with BCrypt,
     * and stores the account flagged for mandatory password change on first login.
     *
     * @param name  the member's full name
     * @param email the member's email address (used as username)
     * @return the generated plain-text password to be sent by email, or null if registration failed
     */
    public String registerNonCommercial(String name, String email) {
        if (name == null || email == null) {
            System.out.println("❌ Name and email cannot be null");
            return null;
        }

        if (name.isBlank() || email.isBlank()) {
            System.out.println("❌ Name and email cannot be blank");
            return null;
        }

        if (memberDAO.emailExists(email)) {
            System.out.println("❌ Email already exists: " + email);
            return null;
        }

        String plainPassword = generatePassword();
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        boolean created = memberDAO.createMember(name, email, hashedPassword, "NON_COMMERCIAL", "APPROVED");
        if (created) {
            System.out.println("✅ Registration successful for " + name);
            return plainPassword;
        } else {
            System.out.println("❌ Registration failed for " + name);
            return null;
        }
    }
}