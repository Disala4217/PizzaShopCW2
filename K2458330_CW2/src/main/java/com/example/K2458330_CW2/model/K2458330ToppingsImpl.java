package com.example.K2458330_CW2.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class K2458330ToppingsImpl implements K2458330Toppings {
    private static final Map<String, Integer> availableToppings = new HashMap<>();
    private final List<String> toppings;

    public K2458330ToppingsImpl() {
        this.toppings = new ArrayList<>();
        availableToppings.put("Pepperoni", 200);
        availableToppings.put("Mushrooms", 150);
        availableToppings.put("Onions", 100);
        availableToppings.put("Sausage", 250);
        availableToppings.put("Bacon", 300);
        availableToppings.put("Extra Cheese", 200);
        availableToppings.put("Black Olives", 180);

    }

    public static Map<String, Integer> getAvailableToppings() {
        return availableToppings;
    }

    @Override
    public List<String> getToppings() {
        return toppings;
    }

    @Override
    public int getTotalToppingCost() {
        int totalCost = 0;
        List<String> toppingList = this.toppings;
        Map<String, Integer> AvailableToppingList = availableToppings;
        for (String topping : toppingList) {
            if (AvailableToppingList.containsKey(topping)) {
                totalCost += AvailableToppingList.get(topping);
                System.out.println("Topping: " + topping + " Price: " + AvailableToppingList.get(topping));
            } else {
                System.out.println("Warning: Topping " + topping + " not found in available toppings list.");
            }
        }

        System.out.println("Total Cost: " + totalCost);
        return totalCost;
    }


    @Override
    public void addTopping(String topping) {
        if (availableToppings.containsKey(topping)) {
            toppings.add(topping);
            System.out.println(topping + " added successfully. Cost: " + availableToppings.get(topping));
        } else {
            System.out.println("Invalid topping: " + topping + ". Please select from available options.");
        }
    }

    public void showToppingDetails() {
        System.out.println("Selected Toppings: ");
        for (String topping : toppings) {
            System.out.println("- " + topping + "(Price: " + availableToppings.get(topping) + ")");
        }
        System.out.println("Total Topping Cost: " + getTotalToppingCost());
    }
}
