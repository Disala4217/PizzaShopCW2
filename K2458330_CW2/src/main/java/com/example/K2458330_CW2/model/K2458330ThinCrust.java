package com.example.K2458330_CW2.model;

public class K2458330ThinCrust implements K2458330Crust {
    @Override
    public String Crust() {
        return "Thin Crust";
    }

    @Override
    public int getCrustPrice() {
        return 500;
    }
}
