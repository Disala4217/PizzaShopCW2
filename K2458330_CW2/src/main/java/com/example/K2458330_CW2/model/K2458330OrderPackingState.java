package com.example.K2458330_CW2.model;

public class K2458330OrderPackingState implements K2458330OrderState {

    @Override
    public String handleOrder(K2458330PickupStrategy strategy) {
        strategy.setState(new K2458330OrderReadyForPickupState());

        return ("Order is being packed...");
    }
}
