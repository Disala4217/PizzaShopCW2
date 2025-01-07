package com.example.K2458330_CW2.model;

import java.util.List;

public interface K2458330PizzaItem {
    K2458330Crust crust();

    K2458330Sauce Sauce();

    List<String> toppings();

    K2458330Cheese cheese();

    int price();
}
