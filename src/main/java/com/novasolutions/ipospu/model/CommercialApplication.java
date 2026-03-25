package com.novasolutions.ipospu.model;

import java.time.LocalDateTime;

// I use a record to keep CommercialApplication immutable and concise.
// Fields map directly to columns in the `commercial_applications` table.
// applicant full_name and company_name live in the linked members row, not here.
public record CommercialApplication(
        long id,
        Long memberId,              // FK to members — set at application time; null until member row exists
        String companiesHouseNumber,
        String directorNames,
        String businessType,
        String businessAddress,
        String email,
        String status,              // PENDING, APPROVED, or REJECTED
        LocalDateTime submittedAt,
        LocalDateTime reviewedAt    // null until an SA reviews the application
) {
}
