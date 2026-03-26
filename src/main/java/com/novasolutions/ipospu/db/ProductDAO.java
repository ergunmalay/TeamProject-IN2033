package com.novasolutions.ipospu.db;

import com.novasolutions.ipospu.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductDAO {
    public List<Product> getAllActiveProducts() {
        String sql = """
            SELECT id, name, description, price, stock_quantity, active
            FROM products
            WHERE active = 1
        """;


        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(new Product(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("stock_quantity"),
                        resultSet.getBoolean("active")
                ));
            }
            return products;
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch products: " + e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
