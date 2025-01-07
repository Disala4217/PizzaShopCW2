package com.example.K2458330_CW2.model;

import com.example.K2458330_CW2.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class K2458330CreditCardPayment implements K2458330PaymentStrategy {
    @Override
    public void processPayment(int orderID, int discount, int finalPayment) {
        String insertOrderQuery = "INSERT INTO `payment`(`order_id`, `total_discount`, `final_price`, `status`) VALUES (?,?,?,?);";
        try (Connection connection = DataBaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(insertOrderQuery)) {
            preparedStatement.setInt(1, orderID);
            preparedStatement.setInt(2, discount);
            preparedStatement.setInt(3, finalPayment);
            preparedStatement.setString(4, "Done");
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Payment recorded successfully.");
            } else {
                throw new SQLException("No rows were inserted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
