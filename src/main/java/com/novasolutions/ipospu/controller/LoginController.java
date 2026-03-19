package com.novasolutions.ipospu.controller;

import com.novasolutions.ipospu.service.MembershipService;

public class LoginController {

    private final MembershipService membershipService = new MembershipService();

    public boolean login(String email, String password) {
        return membershipService.login(email,password);
    }

}
