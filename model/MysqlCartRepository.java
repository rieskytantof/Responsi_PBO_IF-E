package com.pbo.responsi.model;

import com.pbo.responsi.dto.CartItemDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlCartRepository implements CartRepository {

    private final Connection connection;

    public MysqlCartRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<CartItemDTO> findAll() {
        List<CartItemDTO> result = new ArrayList<>();
        String sql = "SELECT name, price, quantity FROM cart_items ORDER BY id ASC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new CartItemDTO(
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void save(CartItemDTO item) {
        String sql = "INSERT INTO cart_items (name, price, quantity) VALUES (?, ?, ?)"
                   + " ON DUPLICATE KEY UPDATE price = VALUES(price), quantity = VALUES(quantity)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());
            stmt.setInt(3, item.getQuantity());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateQuantity(String name, int newQty) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newQty);
            stmt.setString(2, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String name) {
        String sql = "DELETE FROM cart_items WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}