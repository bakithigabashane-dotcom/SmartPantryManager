
package com.example.smartpantrymanager;

import java.util.List;
import java.util.Locale;

public class RecipeMatcher {

    public boolean canMakeRecipe(
            Recipe recipe,
            List<Recipe.Ingredient> pantryItems) {

        for (Recipe.Ingredient required : recipe.getIngredients()) {
            boolean ingredientFound = false;

            for (Recipe.Ingredient available : pantryItems) {
                if (sameIngredient(required.getName(), available.getName())
                        && sameUnit(required.getUnit(), available.getUnit())) {

                    if (available.getQuantity() >= required.getQuantity()) {
                        ingredientFound = true;
                        break;
                    }
                }
            }

            if (!ingredientFound) {
                return false;
            }
        }

        return true;
    }

    private boolean sameIngredient(String first, String second) {
        return normalize(first).equals(normalize(second));
    }

    private boolean sameUnit(String first, String second) {
        return normalize(first).equals(normalize(second));
    }

    private String normalize(String value) {
        String cleaned = value.trim().toLowerCase(Locale.ROOT);

        if (cleaned.endsWith("ies") && cleaned.length() > 3) {
            return cleaned.substring(0, cleaned.length() - 3) + "y";
        }

        if (cleaned.endsWith("s")
                && !cleaned.endsWith("ss")
                && cleaned.length() > 1) {
            return cleaned.substring(0, cleaned.length() - 1);
        }

        return cleaned;
    }
}
