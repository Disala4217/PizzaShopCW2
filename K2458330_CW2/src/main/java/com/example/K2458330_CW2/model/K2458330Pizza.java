package com.example.K2458330_CW2.model;

import java.util.ArrayList;
import java.util.List;

public class K2458330Pizza  {
    private String name;
    private String crust;
    private String sauce;
    private List<String> toppings = new ArrayList<>();
    private String cheese;
    private int CrustPrice;
    private int SaucePrice;
    private int ToppingPrice;
    private int CheesePrice;
    private String Bill;
    private int TotalPrice;

    public String getSauce() {
        return sauce;
    }

    public void setSauce(String sauce) {
        this.sauce = sauce;
    }

    public String getCrust() {
        return crust;
    }

    public void setCrust(String crust) {
        this.crust = crust;
    }

    public List<String> getToppings() {
        return toppings;
    }

    public void setToppings(List<String> toppings) {
        this.toppings = toppings;
    }

    public String getCheese() {
        return cheese;
    }

    public void setCheese(String cheese) {
        this.cheese = cheese;
    }

    public void setSaucePrice(int saucePrice) {
        SaucePrice = saucePrice;
    }

    public void setCrustPrice(int crustPrice) {
        CrustPrice = crustPrice;
    }

    public void setToppingPrice(int toppingPrice) {
        ToppingPrice = toppingPrice;
    }

    public void setCheesePrice(int cheesePrice) {
        CheesePrice = cheesePrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void showItems() {
        System.out.println("Pizza Name: " + name);
        System.out.println("Crust: " + crust);
        System.out.println("Sauce: " + sauce);
        System.out.println("Toppings: " + toppings);
        System.out.println("Cheese: " + cheese);
        System.out.println(Bill());

    }

    public int getTotalPrice() {
        return this.TotalPrice;
    }

    @Override
    public String toString() {
        String result = "Pizza Details:\n" +
                "Name: " + name + "\n" +
                "Crust: " + crust + " - Rs." + CrustPrice + "\n" +
                "Sauce: " + sauce + " - Rs." + SaucePrice + "\n" +
                "Toppings: " + String.join(", ", toppings) + " - Rs." + ToppingPrice + "\n" +
                "Cheese: " + cheese + " - Rs." + CheesePrice + "\n" +
                "Total Price: Rs." + TotalPrice + "\n";
        return result;
    }


    public String Bill() {
        int crustPrice = this.CrustPrice;
        int saucePrice = this.SaucePrice;
        int ToppingPrice = this.ToppingPrice;
        int cheesePrice = this.CheesePrice;

        int totalPrice = crustPrice + saucePrice + ToppingPrice + cheesePrice;
        this.TotalPrice = totalPrice;
        String billDetails = name + "\n" +
                crust + " - Rs." + crustPrice + "\n" +
                sauce + " - Rs." + saucePrice + "\n" +
                String.join(", ", toppings) +
                " - Rs." + ToppingPrice + "\n" +
                cheese + " - Rs." + cheesePrice + "\n" +
                "Total Price: Rs." + totalPrice + "\n";


        this.Bill = billDetails;
        return Bill;
    }

}
