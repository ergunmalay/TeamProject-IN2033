package com.novasolutions.ipospu.db;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {

    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {

            if (conn != null) {
                System.out.println("Database connected successfully");
            }

        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }
}