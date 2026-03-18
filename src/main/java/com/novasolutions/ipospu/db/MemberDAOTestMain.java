package com.novasolutions.ipospu.db;

import com.novasolutions.ipospu.model.Member;

public class MemberDAOTestMain {
    public static void main(String[] args) {
        MemberDAO memberDAO = new MemberDAO();
        Member member = memberDAO.findByEmail("test@example.com");

        if (member != null) {
            System.out.println("✅ Member found:");
            System.out.println(member.getName() + " | " + member.getEmail());
        } else {
            System.out.println("❌ Member not found");
        }
    }
}