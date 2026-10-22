
package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeData {

    private static Recipe.Ingredient item(
            String name, double quantity, String unit) {
        return new Recipe.Ingredient(name, quantity, unit);
    }

    private static Recipe recipe(
            String name, String description,
            String instructions, Recipe.Ingredient... ingredients) {
        return new Recipe(name, description,
                Arrays.asList(ingredients), instructions);
    }

    public static List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        recipes.add(recipe("Egg Fried Rice", "A quick rice and egg meal",
                "Cook the rice. Scramble the eggs, add the rice and stir-fry together.",
                item("rice", 2, "cups"), item("eggs", 2, "items"),
                item("oil", 1, "tbsp")));

        recipes.add(recipe("Tomato Pasta", "Pasta in a simple tomato sauce",
                "Boil pasta. Cook chopped tomatoes in oil, then combine.",
                item("pasta", 200, "g"), item("tomatoes", 2, "items"),
                item("oil", 1, "tbsp")));

        recipes.add(recipe("Banana Oatmeal", "Warm breakfast porridge",
                "Cook oats in milk and top with sliced banana.",
                item("oats", 1, "cup"), item("milk", 1, "cup"),
                item("banana", 1, "item")));

        recipes.add(recipe("Cheese Omelette", "A simple cheesy omelette",
                "Beat the eggs, cook in a pan and add grated cheese.",
                item("eggs", 2, "items"), item("cheese", 30, "g")));

        recipes.add(recipe("Peanut Butter Toast", "Quick toasted bread",
                "Toast the bread and spread peanut butter.",
                item("bread", 2, "slices"),
                item("peanut butter", 2, "tbsp")));

        recipes.add(recipe("Chicken and Rice", "A filling chicken meal",
                "Cook the chicken thoroughly and serve with cooked rice.",
                item("chicken", 200, "g"), item("rice", 1, "cup")));

        recipes.add(recipe("Potato Mash", "Soft mashed potatoes",
                "Boil potatoes until soft, mash with butter and milk.",
                item("potatoes", 3, "items"), item("butter", 1, "tbsp"),
                item("milk", 0.25, "cup")));

        recipes.add(recipe("Tomato Salad", "Fresh tomato salad",
                "Chop tomatoes and cucumber, then mix with oil.",
                item("tomatoes", 2, "items"),
                item("cucumber", 1, "item"), item("oil", 1, "tbsp")));

        recipes.add(recipe("Apple Oat Bowl", "Fruit with oats",
                "Slice the apple and serve with oats and milk.",
                item("apple", 1, "item"), item("oats", 0.5, "cup"),
                item("milk", 0.5, "cup")));

        recipes.add(recipe("Garlic Pasta", "Pasta with garlic oil",
                "Boil pasta. Gently cook garlic in oil and mix together.",
                item("pasta", 200, "g"), item("garlic", 2, "cloves"),
                item("oil", 1, "tbsp")));

        recipes.add(recipe("Chicken Sandwich", "A simple chicken sandwich",
                "Cook chicken thoroughly, then place inside the bread.",
                item("chicken", 100, "g"), item("bread", 2, "slices")));

        recipes.add(recipe("Cheese Toast", "Crispy melted cheese toast",
                "Place cheese on bread and toast until melted.",
                item("bread", 2, "slices"), item("cheese", 40, "g")));

        recipes.add(recipe("Boiled Eggs on Toast", "Breakfast with eggs",
                "Boil eggs until cooked and serve with toasted bread.",
                item("eggs", 2, "items"), item("bread", 2, "slices")));

        recipes.add(recipe("Banana Milk Smoothie", "A creamy fruit drink",
                "Blend banana with milk until smooth.",
                item("banana", 1, "item"), item("milk", 1, "cup")));

        recipes.add(recipe("Buttered Potatoes", "Simple buttered potatoes",
                "Boil potatoes until soft and mix with butter.",
                item("potatoes", 3, "items"), item("butter", 1, "tbsp")));

        recipes.add(recipe("Cucumber Tomato Sandwich",
                "A fresh vegetable sandwich",
                "Slice the vegetables and place them between bread slices.",
                item("bread", 2, "slices"), item("cucumber", 0.5, "item"),
                item("tomatoes", 1, "item")));

        recipes.add(recipe("Peanut Banana Toast", "Sweet breakfast toast",
                "Toast bread and top with peanut butter and sliced banana.",
                item("bread", 2, "slices"),
                item("peanut butter", 1, "tbsp"),
                item("banana", 1, "item")));

        recipes.add(recipe("Garlic Butter Potatoes", "Garlicky potato side",
                "Boil potatoes and toss with butter and cooked garlic.",
                item("potatoes", 3, "items"), item("butter", 1, "tbsp"),
                item("garlic", 1, "clove")));

        recipes.add(recipe("Chicken Pasta", "Chicken served with pasta",
                "Cook chicken thoroughly, boil pasta and combine.",
                item("chicken", 150, "g"), item("pasta", 150, "g")));

        recipes.add(recipe("Apple Peanut Butter Snack",
                "Apple slices with peanut butter",
                "Slice the apple and serve with peanut butter.",
                item("apple", 1, "item"),
                item("peanut butter", 1, "tbsp")));

        return recipes;
    }
}
