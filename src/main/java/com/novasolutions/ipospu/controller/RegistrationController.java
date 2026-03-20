package com.novasolutions.ipospu.controller;

import com.novasolutions.ipospu.service.MembershipService;

public class RegistrationController {

    private final MembershipService membershipService = new MembershipService();

    /**
     * UC-01a: Registers a new non-commercial member.
     *
     * @return the generated plain-text password on success, null on failure
     */
    public String registerNonCommercial(String name, String email) {
        return membershipService.registerNonCommercial(name, email);
    }
}