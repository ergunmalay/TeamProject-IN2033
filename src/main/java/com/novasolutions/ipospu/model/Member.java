package com.novasolutions.ipospu.model;

public class Member {
    private int id;
    private String name;
    private String email;
    private String passwordHash;
    private String memberType;
    private String membershipStatus;
    private boolean isFirstLogin;

    public Member(int id, String name, String email, String passwordHash, String memberType, String membershipStatus, boolean isFirstLogin) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.memberType = memberType;
        this.membershipStatus = membershipStatus;
        this.isFirstLogin = isFirstLogin;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getMemberType() {
        return memberType;
    }

    public String getMembershipStatus() {
        return membershipStatus;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }
}