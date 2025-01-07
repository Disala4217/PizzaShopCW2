package com.example.K2458330_CW2.model;
import java.util.Objects;
import java.util.Scanner;
public class K2458330DeliveryStrategy implements K2458330OrderStrategy {
    @Override
    public void processOrder(K2458330Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        String preparingMessage = "Order is being prepared for delivery to: " + order.getAddress();
        String deliveredMessage = "Delivered to the location: " + order.getAddress();
        order.updateState(preparingMessage);
        System.out.println("press ok to deliver the pizza");
        Scanner scanner = new Scanner(System.in);
        if (Objects.equals(scanner.nextLine(), "ok")) {
            order.updateState(deliveredMessage);
        }
    }
}
