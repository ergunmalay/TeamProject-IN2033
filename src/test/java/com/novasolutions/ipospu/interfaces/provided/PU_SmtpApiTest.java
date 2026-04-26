package com.novasolutions.ipospu.interfaces.provided;

import com.novasolutions.ipospu.impl.PU_SMTP_API;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Component tests for the IPOS-PU sales/email notification path.
 *
 * These tests verify the SMTP_API provided interface by instantiating
 * PU_SMTP_API and interacting with it through the SMTP_API interface.
 *
 * This supports the IPOS-PU notification behaviour used for:
 * - order confirmation emails after successful checkout;
 * - order status update emails;
 * - validation of malformed email notification input.
 */
class PU_SmtpApiTest {

    private SMTP_API smtpApi;

    @BeforeEach
    void setUp() {
        smtpApi = new PU_SMTP_API();
        System.out.println();
        System.out.println("Running IPOS-PU SMTP_API component test...");
    }

    @Test
    @DisplayName("CT-SN-01: Valid order confirmation email is accepted")
    void sendEmail_validInput_returnsTrue() {
        boolean result = smtpApi.sendEmail(
                "ct-sn-01@example.com",
                "Order Confirmation - #999999",
                "Thank you for your order. Status: RECEIVED."
        );

        assertTrue(result,
                "sendEmail should return true for a well-formed message.");

        printPass(
                "CT-SN-01",
                "Valid order confirmation email accepted",
                "The email details were valid, so the notification was accepted successfully."
        );
    }

    @Test
    @DisplayName("CT-SN-02: Blank recipient is rejected")
    void sendEmail_blankRecipient_returnsFalse() {
        boolean result = smtpApi.sendEmail(
                "",
                "Order Confirmation",
                "Body"
        );

        assertFalse(result,
                "sendEmail should reject a blank recipient.");

        printPass(
                "CT-SN-02",
                "Blank recipient rejected",
                "The recipient field was blank, so the email was correctly rejected."
        );
    }

    @Test
    @DisplayName("CT-SN-03: Null email body is rejected")
    void sendEmail_nullBody_returnsFalse() {
        boolean result = smtpApi.sendEmail(
                "ct-sn-03@example.com",
                "Order Confirmation",
                null
        );

        assertFalse(result,
                "sendEmail should reject a null body.");

        printPass(
                "CT-SN-03",
                "Null email body rejected",
                "The email body was missing, so the notification was correctly rejected."
        );
    }

    private void printPass(String testId, String testName, String description) {
        System.out.println("✅ PASS " + testId + " - " + testName);
        System.out.println("  " + description);
        System.out.println("  Interface tested: SMTP_API");
        System.out.println("  Implementation tested: PU_SMTP_API");
    }
}