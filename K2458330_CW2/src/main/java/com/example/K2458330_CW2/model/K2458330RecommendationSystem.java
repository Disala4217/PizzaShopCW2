package com.example.K2458330_CW2.model;

import com.example.K2458330_CW2.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class K2458330RecommendationSystem implements K2458330FeedbackObserver {
    @Override
    public void update(K2458330Order order) {
        System.out.println("Processing feedback for recommendations...");
    }

    public List checkDb() {
        List<Map<String, Object>> responseList = new ArrayList<>();
        String query = "SELECT pizza_id, name, crust, sauce, cheese, toppings, price, rating FROM pizzas ORDER BY rating DESC LIMIT 5;";

        try (Connection connection = DataBaseConnection.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (connection == null) {
                throw new SQLException("Failed to connect to database.");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> pizzaData = new HashMap<>();
                    pizzaData.put("pizza_id", rs.getInt("pizza_id"));
                    pizzaData.put("pizza_name", rs.getString("name"));
                    pizzaData.put("crust", rs.getString("crust"));
                    pizzaData.put("sauce", rs.getString("sauce"));
                    pizzaData.put("cheese", rs.getString("cheese"));
                    pizzaData.put("toppings", rs.getString("toppings"));
                    pizzaData.put("price", rs.getDouble("price"));
                    pizzaData.put("rating", rs.getDouble("rating"));
                    responseList.add(pizzaData);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responseList;
    }
}
