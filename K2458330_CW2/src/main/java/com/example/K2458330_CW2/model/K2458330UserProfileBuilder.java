package com.example.K2458330_CW2.model;

public class K2458330UserProfileBuilder {
    private String userName;
    private String address;

    private int LoyaltyPoints;

    public int getLoyaltyPoints() {
        return LoyaltyPoints;
    }

    public K2458330UserProfileBuilder setLoyaltyPoints(int loyaltyPoints) {
        this.LoyaltyPoints = loyaltyPoints;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public K2458330UserProfileBuilder setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public K2458330UserProfileBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public K2458330UserProfile build() {
        return new K2458330UserProfile(this);
    }
}
