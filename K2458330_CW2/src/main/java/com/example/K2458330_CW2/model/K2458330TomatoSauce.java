package com.example.K2458330_CW2.model;

public class K2458330TomatoSauce implements K2458330Sauce {
    int prize = 400;

    @Override
    public String Sauce() {
        return "Tomato Sauce";
    }

    @Override
    public int getSaucePrice() {
        return 100;
    }
}