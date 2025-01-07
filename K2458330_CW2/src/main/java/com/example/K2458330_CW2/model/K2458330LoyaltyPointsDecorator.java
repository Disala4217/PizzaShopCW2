package com.example.K2458330_CW2.model;

import com.example.K2458330_CW2.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class K2458330LoyaltyPointsDecorator extends K2458330OrderDecorator {
    private int loyaltyPoints;

    public K2458330LoyaltyPointsDecorator(K2458330Order order) {
        super(order);
    }

    @Override
    public void processLoyalty(String Username, int finalPayment, int loyaltyPoints) {
        K2458330UserProfile.setLoyaltyPoints(calculateLoyaltyPoints(Username, finalPayment));
    }


    private int calculateLoyaltyPoints(String Username, int finalPayment) {
        loyaltyPoints = finalPayment / 100;
        String sql = "UPDATE `users` SET `loyalty_points` = `loyalty_points` + ? WHERE `username` = ?";

        try (Connection connection = DataBaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the parameters
            preparedStatement.setInt(1, loyaltyPoints);
            preparedStatement.setString(2, Username);

            // Execute the update
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Loyalty points updated successfully.");
            } else {
                System.out.println("No matching user found with the provided username.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Loyalty Points Earned: " + loyaltyPoints);

        return loyaltyPoints;
    }
}
