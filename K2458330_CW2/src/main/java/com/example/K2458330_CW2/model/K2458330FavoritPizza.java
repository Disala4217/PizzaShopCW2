package com.example.K2458330_CW2.model;
import java.util.ArrayList;
import java.util.List;
public class K2458330FavoritPizza {
    private static final List<String> favoritePizzas = new ArrayList<>();
    public static void addFavoritePizza(String pizzaDetails) {
        if (pizzaDetails == null || pizzaDetails.isEmpty()) {
            System.out.println("Cannot add an empty or null pizza detail.");
            return;
        }
        favoritePizzas.add(pizzaDetails);
        System.out.println("Pizza added to favorites: " + pizzaDetails);
    }
    public static String getFavoritePizzasAsString() {
        if (favoritePizzas.isEmpty()) {
            return "No favorite pizzas added.";
        }
        StringBuilder sb = new StringBuilder();
        for (String pizza : favoritePizzas) {
            sb.append(pizza).append("\n");
        }
        return sb.toString();
    }
    @Override
    public String toString() {
        return getFavoritePizzasAsString();
    }
    public String getFavoritePizza(int index) {
        if (index < 0 || index >= favoritePizzas.size()) {
            System.out.println("Invalid index.");
            return null;
        }
        return favoritePizzas.get(index);
    }
}
