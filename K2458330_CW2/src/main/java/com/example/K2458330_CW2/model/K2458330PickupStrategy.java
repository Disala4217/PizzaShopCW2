package com.example.K2458330_CW2.model;

public class K2458330PickupStrategy implements K2458330OrderStrategy {
    private K2458330OrderState state;

    public K2458330PickupStrategy() {
        this.state = new K2458330OrderPreparingState();

    }

    public void setState(K2458330OrderState state) {
        this.state = state;
    }

    @Override
    public void processOrder(K2458330Order order) {
        if (state != null) {
            System.out.println((state.handleOrder(this)));
            order.updateState((state.handleOrder(this)));

        }
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }


    }
}
