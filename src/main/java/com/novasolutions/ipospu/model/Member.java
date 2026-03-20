package com.novasolutions.ipospu.model;

public record Member(int id,
                     String name,
                     String email,
                     String passwordHash,
                     String memberType,
                     String membershipStatus
) {
}