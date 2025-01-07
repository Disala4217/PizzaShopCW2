package com.example.K2458330_CW2.model;

public class K2458330ChristmasDiscount implements K2458330PromotionStrategy {
    private double Discount;

    public K2458330ChristmasDiscount(K2458330Order order) {
        applyPromotion(order);
    }

    @Override
    public void applyPromotion(K2458330Order order) {
        int total = order.getTotalOrderPrice();
        int discount = (int) (total * 0.1);
        System.out.println("Applied additional 10% discount for Christmas. Discount amount: " + discount);
        this.Discount = discount;
        order.setDiscount(discount);
    }

    public double getDiscount() {
        return Discount;
    }
}
