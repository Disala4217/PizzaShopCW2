package com.example.K2458330_CW2.model;

public class K2458330OrderReadyForPickupState implements K2458330OrderState {
    @Override
    public String handleOrder(K2458330PickupStrategy strategy) {
        return ("Order is ready for pickup!");
    }
}
