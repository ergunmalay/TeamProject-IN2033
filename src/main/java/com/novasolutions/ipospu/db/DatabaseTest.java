package com.novasolutions.ipospu.db;

import java.sql.Connection;

public class DatabaseTest {

    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();

            if (conn != null) {
                System.out.println("✅ Database connected successfully!");
            }

        } catch (Exception e) {
            System.out.println("❌ Connection failed:");
            e.printStackTrace();
        }
    }
}