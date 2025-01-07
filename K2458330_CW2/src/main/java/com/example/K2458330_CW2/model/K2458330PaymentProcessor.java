package com.example.K2458330_CW2.model;

public class K2458330PaymentProcessor {
    private K2458330PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(K2458330PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void pay(int OrderID, int Discount, int Final) {
        if (paymentStrategy != null) {
            paymentStrategy.processPayment(OrderID, Discount, Final);
        } else {
            throw new IllegalStateException("Payment strategy is not set.");
        }
    }


}
