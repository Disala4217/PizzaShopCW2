package com.example.K2458330_CW2.model;

public class K2458330ReorderFavoritePizzaCommand implements K2458330OrderCommand {
    private final K2458330UserProfile userProfile;
    private final String pizzaDetails;

    public K2458330ReorderFavoritePizzaCommand(K2458330UserProfile userProfile, String pizzaDetails) {
        this.userProfile = userProfile;
        this.pizzaDetails = pizzaDetails;
    }

    @Override
    public void execute() {
        System.out.println("Reordering pizza: " + pizzaDetails);
    }
}
