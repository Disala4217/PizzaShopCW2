package com.example.K2458330_CW2.model;

public class K2458330RemoveOrderCommand implements K2458330OrderCommand {
    private final K2458330Order order;

    public K2458330RemoveOrderCommand(K2458330Order order) {
        this.order = order;
    }

    @Override
    public void execute() {
        System.out.println("Removing the order...");
        order.removeOrder();
    }
}
