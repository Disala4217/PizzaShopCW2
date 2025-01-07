package com.example.K2458330_CW2.model;

public class K2458330CheeseContext {
    private final K2458330Cheese pizzaCheese;

    public K2458330CheeseContext(K2458330Cheese pizzaCheese) {
        this.pizzaCheese = pizzaCheese;
    }

    public String SelectCheese() {
        return pizzaCheese.Cheese();
    }

    public int SelectCheesePrice() {
        return pizzaCheese.getCheesePrice();
    }
}
