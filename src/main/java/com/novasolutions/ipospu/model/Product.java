package com.novasolutions.ipospu.model;

public record Product(
        long id,
        String name,
        String description,
        double price,
        int stockQuantity,
        boolean active
) {
}
