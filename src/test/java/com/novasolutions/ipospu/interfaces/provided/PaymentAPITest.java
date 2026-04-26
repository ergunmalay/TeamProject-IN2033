package com.novasolutions.ipospu.interfaces.provided;

import com.novasolutions.ipospu.db.DatabaseConnection;
import com.novasolutions.ipospu.impl.PU_PaymentAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Component tests for the IPOS-PU payment processing path.
 *
 * These tests verify the PaymentAPI provided interface by instantiating
 * PU_PaymentAPI and interacting with it through the PaymentAPI interface.
 *
 * Contract under test (from PU_PaymentAPI):
 *   - processPayment(amount, cardNumber, expiry):
 *     - returns false if amount <= 0;
 *     - returns false if expiry is null, malformed, or in the past;
 *     - on success, inserts an AUTHORISED row into the payments table
 *       and returns true.
 *   - refundPayment(transactionID):
 *     - throws IllegalArgumentException if transactionID <= 0;
 *     - returns false if no payment exists with that ID;
 *     - returns false if the linked order is already SHIPPED or DELIVERED;
 *     - on success, inserts a refund row, marks the order REFUNDED,
 *       and returns true.
 *
 * The valid-payment test verifies the side-effect (a row appearing in the
 * payments table) in addition to the return value. The negative tests
 * verify both the return value and the absence of any database side-effect,
 * confirming that validation rejects invalid input before any database
 * interaction takes place.
 */
class PaymentAPITest {

    private PaymentAPI paymentApi;

    @BeforeEach
    void setUp() {
        paymentApi = new PU_PaymentAPI();
        System.out.println();
        System.out.println("Running IPOS-PU PaymentAPI component test...");
    }

    //
    // processPayment tests
    //

    @Test
    @DisplayName("CT-PA-01: Valid payment is authorised and recorded in the payments table")
    void processPayment_validInput_returnsTrueAndRecordsRow() {
        int rowsBefore = countPaymentRows();

        boolean result = paymentApi.processPayment(50.00, 4111_1111_1111_1111L, "12/28");

        int rowsAfter = countPaymentRows();
        long createdId = mostRecentUnlinkedPaymentId();

        try {
            assertTrue(result, "processPayment should return true for a valid request");
            assertEquals(rowsBefore + 1, rowsAfter,
                    "A new AUTHORISED row should have been inserted into the payments table");

            printPass(
                    "CT-PA-01",
                    "Valid payment authorised",
                    "The payment was authorised and a new row was recorded in the payments table."
            );
        } finally {
            // Tidy up so this test does not leak rows.
            if (createdId > 0) {
                deletePaymentRow(createdId);
            }
        }
    }

    @Test
    @DisplayName("CT-PA-02: Zero amount is rejected before any database interaction")
    void processPayment_zeroAmount_returnsFalse() {
        int rowsBefore = countPaymentRows();

        boolean result = paymentApi.processPayment(0.00, 4111_1111_1111_1111L, "12/28");

        int rowsAfter = countPaymentRows();

        assertFalse(result, "processPayment should reject a zero amount");
        assertEquals(rowsBefore, rowsAfter,
                "No payment row should be created when the amount is zero");

        printPass(
                "CT-PA-02",
                "Zero amount rejected",
                "The amount was not positive, so the payment was correctly rejected."
        );
    }

    @Test
    @DisplayName("CT-PA-03: Expired card is rejected before any database interaction")
    void processPayment_expiredCard_returnsFalse() {
        int rowsBefore = countPaymentRows();

        boolean result = paymentApi.processPayment(50.00, 4111_1111_1111_1111L, "01/20");

        int rowsAfter = countPaymentRows();

        assertFalse(result, "processPayment should reject a card with an expired date");
        assertEquals(rowsBefore, rowsAfter,
                "No payment row should be created when the card is expired");

        printPass(
                "CT-PA-03",
                "Expired card rejected",
                "The card expiry date was in the past, so the payment was correctly rejected."
        );
    }

    @Test
    @DisplayName("CT-PA-04: Malformed expiry value is rejected before any database interaction")
    void processPayment_malformedExpiry_returnsFalse() {
        int rowsBefore = countPaymentRows();

        boolean result = paymentApi.processPayment(50.00, 4111_1111_1111_1111L, "not-a-date");

        int rowsAfter = countPaymentRows();

        assertFalse(result, "processPayment should reject a malformed expiry value");
        assertEquals(rowsBefore, rowsAfter,
                "No payment row should be created when the expiry is malformed");

        printPass(
                "CT-PA-04",
                "Malformed expiry rejected",
                "The expiry value was not in MM/YY format, so the payment was correctly rejected."
        );
    }

    // ─────────────────────────────────────────────────────────────
    // refundPayment tests
    // ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("CT-PA-05: Refund of a non-existent transaction is rejected")
    void refundPayment_nonExistentTransaction_returnsFalse() {
        // Use a transaction ID that is extremely unlikely to exist in the test database.
        int unlikelyTransactionId = Integer.MAX_VALUE - 7;

        boolean result = paymentApi.refundPayment(unlikelyTransactionId);

        assertFalse(result,
                "refundPayment should return false when no payment exists for the given transaction ID");

        printPass(
                "CT-PA-05",
                "Non-existent refund rejected",
                "No payment row was found for the given transaction ID, so the refund was correctly rejected."
        );
    }

    @Test
    @DisplayName("CT-PA-06: Refund with a non-positive transaction ID throws IllegalArgumentException")
    void refundPayment_zeroTransactionId_throwsIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> paymentApi.refundPayment(0),
                "refundPayment should reject a non-positive transaction ID with IllegalArgumentException"
        );

        printPass(
                "CT-PA-06",
                "Invalid transaction ID rejected",
                "The transaction ID was not positive, so the refund was correctly rejected with an IllegalArgumentException."
        );
    }

    // ─────────────────────────────────────────────────────────────
    // helpers
    // ─────────────────────────────────────────────────────────────

    private int countPaymentRows() {
        String sql = "SELECT COUNT(*) FROM payments";
        try (Connection conn = DatabaseConnection.getInstance().getPuConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : -1;
        } catch (SQLException e) {
            System.out.println("Could not count payments rows.");
            System.out.println(e.getMessage());
            return -1;
        }
    }

    private long mostRecentUnlinkedPaymentId() {
        // The valid-payment test creates a row with order_id IS NULL. We pick
        // the most recent such row so we can clean up exactly the row we
        // created without affecting any pre-existing demo data.
        String sql = "SELECT id FROM payments WHERE order_id IS NULL ORDER BY id DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getInstance().getPuConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getLong(1) : -1;
        } catch (SQLException e) {
            return -1;
        }
    }

    private void deletePaymentRow(long paymentId) {
        String sql = "DELETE FROM payments WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getPuConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, paymentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Cleanup warning: payment row may remain (id=" + paymentId + ")");
            System.out.println(e.getMessage());
        }
    }

    private void printPass(String testId, String testName, String description) {
        System.out.println("✅ PASS " + testId + " - " + testName);
        System.out.println("  " + description);
        System.out.println("  Interface tested: PaymentAPI");
        System.out.println("  Implementation tested: PU_PaymentAPI");
    }
}
