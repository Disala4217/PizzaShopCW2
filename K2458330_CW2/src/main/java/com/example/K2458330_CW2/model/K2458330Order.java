package com.example.K2458330_CW2.model;

import com.example.K2458330_CW2.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class K2458330Order {
    private static int totalOrderPrice;
    private static int Discount;
    private int orderID;
    private final List<K2458330Pizza> pizzas;
    private String address;
    private K2458330OrderStrategy orderStrategy;
    private boolean isOrderActive;
    private String payment = "pending";
    private int finalPayment;
    private final List<K2458330FeedbackObserver> observers = new ArrayList<>();
    private final List<String> feedbackList = new ArrayList<>();
    private int totalRating = 0;
    private int ratingCount = 0;

    public K2458330Order() {
        this.pizzas = new ArrayList<>();
        this.isOrderActive = true;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void addObserver(K2458330FeedbackObserver observer) {
        observers.add(observer);
    }

    public void updateState(String state) {
        String sql = "UPDATE `orders` SET `state` = ? WHERE `order_id` = ?;";

        try {
            Connection conn = DataBaseConnection.connect();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, state);
                stmt.setInt(2, this.orderID);

                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Order state updated successfully.");
                } else {
                    System.out.println("No matching order found with the provided order_id.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                DataBaseConnection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeObserver(K2458330FeedbackObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (K2458330FeedbackObserver observer : observers) {
            observer.update(this);
        }
    }

    public void addFeedback(String feedback, int rating) {
        feedbackList.add(feedback);
        totalRating += rating;
        ratingCount++;
        notifyObservers();
    }

    public double getAverageRating() {
        return ratingCount == 0 ? 0 : (double) totalRating / ratingCount;
    }

    public List<String> getFeedbackList() {
        return feedbackList;
    }
    public int getFinalPayment() {
        this.finalPayment = getTotalOrderPrice() - Discount;
        return finalPayment;
    }
    public int getDiscount() {
        return Discount;
    }
    public void setDiscount(int Discount) {
        K2458330Order.Discount += Discount;

    }
    public void getOrderdPizza() {
        System.out.println("Available Pizzas:");
        for (K2458330Pizza pizza : pizzas) {
            System.out.println(pizza);
        }
    }
    public void setPayment() {
        this.payment = "Done";
    }
    public int getTotalOrderPrice() {
        String query = "SELECT total_price FROM orders WHERE order_id = ?";
        int totalOrderPrice = 0; // Default value in case of failure

        try (Connection connection = DataBaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, getOrderID());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    totalOrderPrice = resultSet.getInt("total_price");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalOrderPrice;
    }
    public void processLoyalty(String userName, int finalPayment, int loyaltyPoints) {

    }
    public void addPizza(K2458330Pizza pizza) {
        if (pizza != null) {
            pizzas.add(pizza);
        }
    }
    public void removePizza(K2458330Pizza pizza) {
        pizzas.remove(pizza);
    }

    public void setOrderStrategy(K2458330OrderStrategy orderStrategy) {
        this.orderStrategy = orderStrategy;
    }
    public void processOrder() {
        if (orderStrategy != null) {
            orderStrategy.processOrder(this);
        } else {
            System.out.println("No strategy set for processing the order.");
        }
    }
    public void removeOrder() {
        this.isOrderActive = false;
        System.out.println("Order has been removed.");
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }


}
