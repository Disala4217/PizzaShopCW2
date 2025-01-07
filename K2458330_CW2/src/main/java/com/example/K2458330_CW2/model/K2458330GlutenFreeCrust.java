package com.example.K2458330_CW2.model;

public class K2458330GlutenFreeCrust implements K2458330Crust {
    int prize = 1000;

    @Override
    public String Crust() {
        return "Gluten-Free Crust";
    }

    @Override
    public int getCrustPrice() {
        return 600;
    }
}
