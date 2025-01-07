package com.example.K2458330_CW2.model;

public class K2458330PlaceOrderCommand implements K2458330OrderCommand {
    private final K2458330Order order;

    public K2458330PlaceOrderCommand(K2458330Order order) {
        this.order = order;
    }

    @Override
    public void execute() {
        System.out.println("Placing order...");
        order.updateState("Placing order...");
        order.processOrder();
    }
}
