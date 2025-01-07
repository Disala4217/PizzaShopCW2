package com.example.K2458330_CW2.model;

import com.example.K2458330_CW2.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class K2458330PizzaBuilder {

    private String name;
    private String crust;
    private int crustPrice;
    private String sauce;
    private int saucePrice;
    private String cheese;
    private int cheesePrice;
    private List<String> toppings;
    private int toppingsPrice;

    // Setter for the crust
    public K2458330PizzaBuilder setCrust(String crust, int crustPrice) {
        this.crust = crust;
        this.crustPrice = crustPrice;
        return this;
    }

    // Setter for the sauce
    public K2458330PizzaBuilder setSauce(String sauce, int saucePrice) {
        this.sauce = sauce;
        this.saucePrice = saucePrice;
        return this;
    }

    // Setter for the cheese
    public K2458330PizzaBuilder setCheese(String cheese, int cheesePrice) {
        this.cheese = cheese;
        this.cheesePrice = cheesePrice;
        return this;
    }

    public K2458330PizzaBuilder setToppings(List<String> toppings, int toppingsPrice) {
        this.toppings = toppings;
        this.toppingsPrice = toppingsPrice;
        return this;
    }

    public K2458330Pizza build() {
        if (crust == null || sauce == null || cheese == null || toppings == null) {
            throw new IllegalStateException("Missing required fields to build the pizza.");
        }

        K2458330Pizza pizza = new K2458330Pizza();
        pizza.setName(this.name);
        pizza.setCrust(this.crust);
        pizza.setCrustPrice(this.crustPrice);
        pizza.setSauce(this.sauce);
        pizza.setSaucePrice(this.saucePrice);
        pizza.setCheese(this.cheese);
        pizza.setCheesePrice(this.cheesePrice);
        pizza.setToppings(this.toppings);
        pizza.setToppingPrice(this.toppingsPrice);

        return pizza;
    }

    public void save(String name, String crust, String sauce, String cheese, String toppings, double price, double rating, String pizzaType) {
        Connection connection = DataBaseConnection.connect();
        String insertPizzaQuery = "INSERT INTO pizzas (name, crust, sauce, cheese, toppings, price, rating, pizza_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertPizzaQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, crust);
            preparedStatement.setString(3, sauce);
            preparedStatement.setString(4, cheese);
            preparedStatement.setString(5, toppings);
            preparedStatement.setDouble(6, price);
            preparedStatement.setDouble(7, rating);
            preparedStatement.setString(8, pizzaType);

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

    public String getName() {
        return name;
    }

    public K2458330PizzaBuilder setName(String name) {
        this.name = name;
        return this;
    }
}
