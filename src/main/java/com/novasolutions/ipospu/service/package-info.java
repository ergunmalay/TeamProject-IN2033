/**
 * Business logic layer.
 *
 * Service classes contain the core logic that isn't just CRUD operations.
 * For example, OrderService handles the full checkout flow: stock check ->
 * payment -> stock deduction -> email -> order creation.
 *
 * Services call DAOs (for our own database) and adapters (for cross-subsystem
 * data via shared DB).
 *
 * Planned classes:
 * - OrderService        (checkout flow, order lifecycle - UC-08)
 * - MembershipService   (registration, login, password rules - UC-01a/b, UC-02, UC-03)
 * - PromotionService    (campaign CRUD, discount application - UC-13/14/15/16)
 * - ReportService       (report generation queries - UC-17/18/19)
 */
package com.novasolutions.ipospu.service;
