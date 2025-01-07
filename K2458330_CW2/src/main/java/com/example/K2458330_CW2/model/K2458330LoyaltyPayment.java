package com.example.K2458330_CW2.model;

import com.example.K2458330_CW2.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class K2458330LoyaltyPayment implements K2458330PaymentStrategy {
    private final String userName;
    private final int finalPayment;

    public K2458330LoyaltyPayment(String userName, int finalPayment) {
        this.userName = userName;
        this.finalPayment = finalPayment;
    }

    @Override
    public void processPayment(int orderID, int discount, int finalPrice) {
        String resultMessage = "Payment failed.";
        String selectQuery = "SELECT loyalty_points FROM users WHERE username = ?";
        String updateQuery = "UPDATE users SET loyalty_points = loyalty_points - ? WHERE username = ?";
        String insertOrderQuery = "INSERT INTO payment (order_id, total_discount, final_price, status) VALUES (?, ?, ?, ?)";

        try (Connection connection = DataBaseConnection.connect();
             PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
             PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
             PreparedStatement orderStmt = connection.prepareStatement(insertOrderQuery)) {

            selectStmt.setString(1, userName);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                if (resultSet.next()) {
                    int loyaltyPoints = resultSet.getInt("loyalty_points");

                    if (loyaltyPoints >= finalPayment) {
                        updateStmt.setInt(1, finalPayment);
                        updateStmt.setString(2, userName);

                        int rowsUpdated = updateStmt.executeUpdate();
                        if (rowsUpdated > 0) {
                            // Step 4: Insert order details into the database
                            orderStmt.setInt(1, orderID);
                            orderStmt.setInt(2, discount);
                            orderStmt.setInt(3, finalPrice);
                            orderStmt.setString(4, "Done");

                            int rowsInserted = orderStmt.executeUpdate();
                            if (rowsInserted > 0) {
                                resultMessage = "Payment successful. Loyalty points deducted.";
                            }
                        } else {
                            resultMessage = "Failed to deduct loyalty points.";
                        }
                    } else {
                        resultMessage = "Insufficient loyalty points for this payment.";
                    }
                } else {
                    resultMessage = "User not found.";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resultMessage = "Database error occurred.";
        }

    }
}
