package com.novasolutions.ipospu.db;

import com.novasolutions.ipospu.model.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// I use this DAO to handle all direct database operations for the members table.
// I keep SQL logic here so the service layer stays free of JDBC concerns.
public class MemberDAO {

    // I look up a member by their email address — used during login and duplicate checks.
    public Member findByEmail(String email) {
        String sql = "SELECT * FROM members WHERE email = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // I map each column to the corresponding Member record field.
                    // I use 'full_name' here because I renamed the column from 'name'.
                    return new Member(
                            resultSet.getLong("id"),
                            resultSet.getString("full_name"),
                            resultSet.getString("email"),
                            resultSet.getString("password_hash"),
                            resultSet.getString("member_type"),
                            resultSet.getString("membership_status"),
                            resultSet.getString("company_name"),
                            resultSet.getInt("order_count"),
                            resultSet.getBoolean("is_first_login"),
                            resultSet.getTimestamp("created_at").toLocalDateTime()
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // I check email existence separately to avoid fetching a full Member object just for a duplicate check.
    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM members WHERE email = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // I insert a new member row. I omit order_count and is_first_login
    // because the DB handles their defaults (0 and 1 respectively).
    public boolean createMember(String fullName, String email, String passwordHash, String memberType, String membershipStatus) {
        String sql = "INSERT INTO members (full_name, email, password_hash, member_type, membership_status, created_at) VALUES (?, ?, ?, ?, ?, NOW())";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, passwordHash);
            preparedStatement.setString(4, memberType);
            preparedStatement.setString(5, membershipStatus);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Member created: " + email);
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    // I use this primarily in tests to clean up member rows by email.
    public boolean deleteMemberByEmail(String email) {
        String sql = "DELETE FROM members WHERE email = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
