package com.example.K2458330_CW2.model;
import com.example.K2458330_CW2.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class K2458330DigitalWalletPayment implements K2458330PaymentStrategy {
    public K2458330DigitalWalletPayment() {
    }
    @Override
    public void processPayment(int OrderID, int Discount, int Final) {
        String insertOrderQuery = "INSERT INTO `credit_card_payment`(`order_id`, `total_discount`, `final_price`, `status`) VALUES (?,?,?,?);";
        try (Connection connection = DataBaseConnection.connect();
             PreparedStatement orderStatement = connection.prepareStatement(insertOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            orderStatement.setInt(1, OrderID);
            orderStatement.setInt(2, Discount);
            orderStatement.setInt(3, Final);
            orderStatement.setString(4, "Done");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
