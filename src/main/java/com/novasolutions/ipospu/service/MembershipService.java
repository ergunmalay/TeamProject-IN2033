package com.novasolutions.ipospu.service;

import com.novasolutions.ipospu.db.MemberDAO;
import com.novasolutions.ipospu.model.Member;

import java.util.Objects;

public class MembershipService {

    private final MemberDAO memberDAO = new MemberDAO();

    public boolean login(String email, String password) {
        // First check if email or password is null
        if (email == null || password == null ) {
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

        // compare passwords

        if (Objects.equals(member.getPasswordHash(), password)) {
            System.out.println("✅ Login successful for " + member.getName());
            return true;
        } else {
            System.out.println("❌ Invalid email or password");
            return false;
        }
    }

    public boolean registerNonCommercial(String name, String email, String password) {
        String memberType = "NonCommercial";
        String membershipStatus = "Approved";

        if (email == null || password == null || name == null) {
            System.out.println("❌ Email and password cannot be empty");
            return false;
        }

        if (email.isBlank() || password.isBlank() || name.isBlank()) {
            System.out.println("❌ Email and password cannot be blank");
            return false;
        }

        // Check if email already exists
        if (memberDAO.emailExists(email)) {
            System.out.println("❌ Email already exists: " + email);
            return false;
        }

        // Create new member
        boolean created = memberDAO.createMember(name, email, password, memberType, membershipStatus);
        if (created) {
            System.out.println("✅ Registration successful for " + name);
            return true;
        } else {
            System.out.println("❌ Registration failed for " + name);
            return false;
        }
    }
}
