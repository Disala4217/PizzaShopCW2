package com.example.K2458330_CW2.model;

import com.example.K2458330_CW2.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class K2458330FeedbackPizza implements K2458330FeedBackCommand {
    private final int rating;
    private final int pizzaId;

    public K2458330FeedbackPizza(int rating, int pizzaId) {
        this.rating = rating;
        this.pizzaId = pizzaId;
    }

    @Override
    public void execute() {
        String query = "UPDATE `pizzas` SET `rating` = ((`rating` * `rating count`) + ?) / (`rating count` + 1), " +
                "`rating count` = `rating count` + 1 WHERE `pizza_id` = ?";

        try (Connection connection = DataBaseConnection.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, rating);
            stmt.setInt(2, pizzaId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Pizza rating and count updated successfully.");
            } else {
                System.out.println("No rows affected. Check if pizza ID exists.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating pizza rating.");
        }
    }
}
