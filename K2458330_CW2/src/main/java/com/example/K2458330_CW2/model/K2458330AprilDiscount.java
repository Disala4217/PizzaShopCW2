package com.example.K2458330_CW2.model;

public class K2458330AprilDiscount implements K2458330PromotionStrategy {
    public K2458330AprilDiscount(K2458330Order order) {
        applyPromotion(order);
    }


    @Override
    public void applyPromotion(K2458330Order order) {
        int total = order.getTotalOrderPrice();
        int discount = (int) (total * 0.1);
        System.out.println("Applied additional 20% discount for New Year Season. Discount amount: " + discount);
        order.setDiscount(discount);
    }
}
