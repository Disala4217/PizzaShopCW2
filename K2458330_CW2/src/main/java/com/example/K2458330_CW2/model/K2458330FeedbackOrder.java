package com.example.K2458330_CW2.model;

import com.example.K2458330_CW2.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class K2458330FeedbackOrder implements K2458330FeedBackCommand {
    private final int rating;
    private final int orderId;

    public K2458330FeedbackOrder(int rating, int orderId) {
        this.rating = rating;
        this.orderId = orderId;
    }

    @Override
    public void execute() {
        try (Connection connection = DataBaseConnection.connect()) {
            if (connection == null) {
                throw new SQLException("Unable to connect to the database.");
            }

            String query = "UPDATE `orders` SET `rating` = ((`rating` * `rating count`) + ?) / (`rating count` + 1), " +
                    "`rating count` = `rating count` + 1 WHERE `order_id` = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, rating);
                stmt.setInt(2, orderId);

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Order rating updated successfully.");
                } else {
                    System.out.println("No rows affected. Check if the order ID exists.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
