package com.example.K2458330_CW2.model;

public class K2458330ContextSauce {
    private final K2458330Sauce PizzaSauce;

    public K2458330ContextSauce(K2458330Sauce PizzaSauce) {
        this.PizzaSauce = PizzaSauce;
    }

    public String SelectSauce() {
        return PizzaSauce.Sauce();
    }

    public int SelectSaucePrice() {
        return PizzaSauce.getSaucePrice();
    }
}
