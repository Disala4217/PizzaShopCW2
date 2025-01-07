package com.example.K2458330_CW2.model;


public class K2458330OrderDecorator extends K2458330Order {
    protected K2458330Order order;
    protected K2458330UserProfile userProfile;

    public K2458330OrderDecorator(K2458330Order order) {

        super();
        this.order = order;

    }

    public void processLoyalty(String userName, int finalPayment, int loyaltyPoints) {
        order.processLoyalty(userProfile.getUserName(), order.getFinalPayment(), userProfile.getLoyaltyPoints());
    }
}
