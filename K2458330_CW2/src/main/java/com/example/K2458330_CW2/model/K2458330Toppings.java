package com.example.K2458330_CW2.model;

import java.util.List;

public interface K2458330Toppings {
    List<String> getToppings(); // Get the list of selected toppings

    void addTopping(String topping); // Add a topping to the list

    int getTotalToppingCost(); // Get the total cost of selected toppings
}
