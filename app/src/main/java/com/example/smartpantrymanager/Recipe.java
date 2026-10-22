
package com.example.smartpantrymanager;

import java.util.List;

public class Recipe {

    private final String name;
    private final String description;
    private final List<Ingredient> ingredients;
    private final String instructions;

    public Recipe(String name, String description,
                  List<Ingredient> ingredients, String instructions) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public static class Ingredient {
        private final String name;
        private final double quantity;
        private final String unit;

        public Ingredient(String name, double quantity, String unit) {
            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
        }

        public String getName() {
            return name;
        }

        public double getQuantity() {
            return quantity;
        }

        public String getUnit() {
            return unit;
        }
    }
}
