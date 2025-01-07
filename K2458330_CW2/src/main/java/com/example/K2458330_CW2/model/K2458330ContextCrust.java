package com.example.K2458330_CW2.model;

public class K2458330ContextCrust {
    private final K2458330Crust PizzaCrust;

    public K2458330ContextCrust(K2458330Crust PizzaCrust) {
        this.PizzaCrust = PizzaCrust;
    }

    public String SelectCrust() {
        return PizzaCrust.Crust();
    }

    public int SelectCrustPrice() {
        return PizzaCrust.getCrustPrice();
    }
}
