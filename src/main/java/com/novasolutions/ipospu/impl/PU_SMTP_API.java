package com.novasolutions.ipospu.impl;

import com.novasolutions.ipospu.db.DatabaseConnection;
import com.novasolutions.ipospu.interfaces.provided.SMTP_API;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Implementation of SMTP_API provided by IPOS-PU.
 * Attempts to send email via an external SMTP server.
 * If the server is unavailable, stores the email in the email_queue table for later retry.
 */
public class PU_SMTP_API implements SMTP_API {

    /**
     * Sends an email to the specified recipient.
     * In this implementation, email sending is simulated — the email is written
     * to the email_queue table with status SENT (simulating successful delivery).
     * In production, integrate a real SMTP library (e.g. Jakarta Mail).
     *
     * @param recipient the email address of the recipient
     * @param subject   the subject line
     * @param body      the body content
     * @return true if stored/sent successfully, false on failure
     */
    @Override
    public boolean sendEmail(String recipient, String subject, String body) {
        if (recipient == null || recipient.isBlank()) return false;
        if (subject == null) return false;
        if (body == null) return false;

        String sql = """
                INSERT INTO email_queue (recipient, subject, body, status, created_at, sent_at)
                VALUES (?, ?, ?, 'SENT', ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getInstance().getPuConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            LocalDateTime now = LocalDateTime.now();
            ps.setString(1, recipient);
            ps.setString(2, subject);
            ps.setString(3, body);
            ps.setObject(4, now);
            ps.setObject(5, now);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            // SMTP unavailable — store for later retry
            return storeForRetry(recipient, subject, body);
        }
    }

    /**
     * Stores a failed email in the queue with status PENDING for later retry.
     */
    private boolean storeForRetry(String recipient, String subject, String body) {
        String sql = """
                INSERT INTO email_queue (recipient, subject, body, status, created_at)
                VALUES (?, ?, ?, 'PENDING', ?)
                """;
        try (Connection conn = DatabaseConnection.getInstance().getPuConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, recipient);
            ps.setString(2, subject);
            ps.setString(3, body);
            ps.setObject(4, LocalDateTime.now());
            ps.executeUpdate();
            return false;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}