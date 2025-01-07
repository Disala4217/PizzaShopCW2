package com.example.K2458330_CW2.model;

public class K2558330DiscountDecorator extends K2458330OrderDecorator {
    private int discount;
    private int loyaltyPoints;
    private int finalPayment;

    public K2558330DiscountDecorator(K2458330Order order) {
        super(order);
    }

    @Override
    public int getDiscount() {
        return discount;
    }

    @Override
    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public void processLoyalty(String userName, int finalPayment, int loyaltyPoints) {
        this.finalPayment = finalPayment;
        this.loyaltyPoints = loyaltyPoints;
        calculateDiscount();
        super.setDiscount(discount);
        System.out.println("Discount: " + discount);
    }

    private void calculateDiscount() {
        int total = finalPayment;
        System.out.println("Total: " + super.getTotalOrderPrice());
        if (loyaltyPoints > 10) {
            setDiscount(total * 10 / 100);
        } else if (loyaltyPoints > 5) {
            setDiscount(total / 100);
        } else {
            discount = 0;
        }
    }
}


