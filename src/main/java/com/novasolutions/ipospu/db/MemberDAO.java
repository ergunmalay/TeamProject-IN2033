package com.novasolutions.ipospu.db;

import com.novasolutions.ipospu.model.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDAO {

    public Member findByEmail(String email) {
        String sql = "SELECT * FROM members WHERE email = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Member(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password_hash"),
                            resultSet.getString("member_type"),
                            resultSet.getString("membership_status")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM members WHERE email = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Returns true if email exists
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createMember(String name, String email, String passwordHash, String memberType, String membershipStatus) {
        String sql = "INSERT INTO members (name, email, password_hash, member_type, membership_status) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
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

    public boolean deleteMemberByEmail(String email) {
        String sql = "DELETE FROM members WHERE email = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Returns true if delete was successful

        } catch (Exception e) {
            e.printStackTrace();
        }
         return false;
    }

}