
package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class RecipeSuggestionsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private final List<Recipe> matchingRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new DatabaseHelper(this);
        createScreen();
        loadMatchingRecipes();
    }

    private void createScreen() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 16);

        TextView heading = new TextView(this);
        heading.setText("Recipes You Can Make");
        heading.setTextSize(25);
        heading.setGravity(Gravity.CENTER_VERTICAL);
        heading.setPadding(0, 0, 0, 16);
        layout.addView(heading);

        TextView explanation = new TextView(this);
        explanation.setText(
                "Suggestions use your pantry. Every required ingredient " +
                        "must be available in a sufficient quantity.");
        explanation.setTextSize(15);
        explanation.setPadding(0, 0, 0, 20);
        layout.addView(explanation);

        ListView recipeList = new ListView(this);
        recipeList.setId(android.view.View.generateViewId());

        layout.addView(recipeList, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0, 1));

        setContentView(layout);

        recipeList.setOnItemClickListener((parent, view, position, id) -> {
            Recipe selected = matchingRecipes.get(position);

            Toast.makeText(this,
                    selected.getName() + ": " + selected.getInstructions(),
                    Toast.LENGTH_LONG).show();
        });

        recipeList.setTag("recipeList");
    }

    private void loadMatchingRecipes() {
        List<Recipe.Ingredient> pantryItems = new ArrayList<>();

        try (Cursor cursor = databaseHelper.getAllIngredients()) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name"));
                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("quantity"));
                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow("unit"));

                pantryItems.add(
                        new Recipe.Ingredient(name, quantity, unit));
            }
        }

        RecipeMatcher matcher = new RecipeMatcher();

        for (Recipe recipe : RecipeData.getRecipes()) {
            if (matcher.canMakeRecipe(recipe, pantryItems)) {
                matchingRecipes.add(recipe);
            }
        }

        ListView recipeList = findListView();
        List<String> displayNames = new ArrayList<>();

        for (Recipe recipe : matchingRecipes) {
            displayNames.add(
                    recipe.getName() + "\n" + recipe.getDescription());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayNames);

        recipeList.setAdapter(adapter);

        if (matchingRecipes.isEmpty()) {
            Toast.makeText(this,
                    "No complete recipe matches yet. Add more ingredients!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private ListView findListView() {
        LinearLayout layout = (LinearLayout)
                ((android.view.ViewGroup) findViewById(
                        android.R.id.content)).getChildAt(0);

        for (int i = 0; i < layout.getChildCount(); i++) {
            if (layout.getChildAt(i) instanceof ListView) {
                return (ListView) layout.getChildAt(i);
            }
        }

        throw new IllegalStateException("Recipe list was not created");
    }

    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        super.onDestroy();
    }
}
