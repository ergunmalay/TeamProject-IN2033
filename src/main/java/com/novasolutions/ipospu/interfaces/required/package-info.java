/**
 * Interfaces REQUIRED by IPOS-PU from other subsystems.
 *
 * These define the contracts for accessing IPOS-SA and IPOS-CA functionality.
 * IPOS-PU does NOT implement these — Teams 22 and 23 provide the real implementations.
 *
 * For our system, we write ADAPTER classes (in the impl/ package) that implement
 * these interfaces by reading/writing to the shared MySQL database.
 *
 * Required from IPOS-SA:
 * - I_CommercialApproval    (submit/check commercial membership applications)
 * - I_MerchantAccount       (product catalogue, order placement, invoices)
 *
 * Required from IPOS-CA:
 * - I_Inventory             (check stock, deduct stock after purchase)
 *
 * These must match the Week 5 deliverable EXACTLY.
 */
package com.novasolutions.ipospu.interfaces.required;
