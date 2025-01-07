package com.example.K2458330_CW2.model;

import com.example.K2458330_CW2.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class K2458330UserProfile implements K2458330User {
    private static int loyaltyPoints;
    private final String userName;
    private String address;
    private final K2458330FavoritPizza favoritePizzas;

    public K2458330UserProfile(K2458330UserProfileBuilder builder) {
        this.userName = builder.getUserName();
        this.address = builder.getAddress();
        loyaltyPoints = builder.getLoyaltyPoints();
        this.favoritePizzas = new K2458330FavoritPizza();
    }


    public int getLoyaltyPoints() {

        return loyaltyPoints;
    }

    public static void setLoyaltyPoints(int loyaltyPoint) {
        loyaltyPoints = loyaltyPoint;
    }

    public void updateLoyeltyPoints() {
        Connection connection = DataBaseConnection.connect();
        String selectQuery = "SELECT `loyalty_points` FROM `users` WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, this.userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                loyaltyPoints = resultSet.getInt("loyalty_points");
                System.out.println("Loyalty points updated to: " + loyaltyPoints);
            } else {
                System.out.println("No user found with the username: " + this.userName);
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching loyalty points:");
            e.printStackTrace();
        } finally {
            DataBaseConnection.disconnect();
        }
    }

    @Override
    public void update() {
        System.out.println("Alert (Client): ");
    }

    public String getUserName() {
        return userName;
    }

    public String getAddress() {

        return address;
    }


    public void SaveFavoritePizza(String pizzaName) {
        Connection connection = DataBaseConnection.connect();
        String insertQuery = "INSERT INTO user_favorite_pizzas (user_id, pizza_id) VALUES ((SELECT user_id FROM users WHERE username = ?), (SELECT pizza_id FROM pizzas WHERE name = ?))";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, this.userName);
            preparedStatement.setString(2, pizzaName);


            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Pizza saved successfully!");
            } else {
                System.out.println("Failed to save the pizza.");
            }
        } catch (SQLException e) {
            System.out.println("Error while saving pizza:");
            e.printStackTrace();
        } finally {
            DataBaseConnection.disconnect();
        }


    }

    public String getFavorite() {
        return K2458330FavoritPizza.getFavoritePizzasAsString();
    }

}
