
package com.example.smartpantrymanager;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class RecipeSuggestionsActivity extends AppCompatActivity {

    private static final int CREAM = Color.rgb(247, 245, 239);
    private static final int GREEN = Color.rgb(49, 91, 69);
    private static final int MUTED = Color.rgb(101, 117, 104);
    private static final int WHITE = Color.WHITE;

    private DatabaseHelper databaseHelper;
    private final List<Recipe> matchingRecipes = new ArrayList<>();
    private ListView recipeList;
    private LinearLayout layout;
    private TextView emptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new DatabaseHelper(this);
        createScreen();
        loadMatchingRecipes();
    }

    private void createScreen() {
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 28, 24, 16);
        layout.setBackgroundColor(CREAM);

        TextView heading = new TextView(this);
        heading.setText("Recipes You Can Make");
        heading.setTextSize(27);
        heading.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        heading.setTextColor(GREEN);
        heading.setPadding(0, 0, 0, 8);
        layout.addView(heading);

        TextView explanation = new TextView(this);
        explanation.setText(
                "Make something delicious with your pantry ingredients. " +
                        "Every required ingredient must be available in sufficient quantity."
        );
        explanation.setTextSize(15);
        explanation.setTextColor(MUTED);
        explanation.setPadding(0, 0, 0, 20);
        layout.addView(explanation);

        emptyMessage = new TextView(this);
        emptyMessage.setText(
                "No complete recipe matches yet.\n\n" +
                        "Add more ingredients or check the quantities in your pantry."
        );
        emptyMessage.setTextSize(17);
        emptyMessage.setTextColor(GREEN);
        emptyMessage.setGravity(Gravity.CENTER);
        emptyMessage.setPadding(20, 32, 20, 32);
        emptyMessage.setVisibility(View.GONE);
        layout.addView(emptyMessage);

        recipeList = new ListView(this);
        recipeList.setDivider(null);
        recipeList.setDividerHeight(0);
        recipeList.setBackgroundColor(CREAM);
        recipeList.setClipToPadding(false);

        layout.addView(recipeList, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1
        ));

        setContentView(layout);

        recipeList.setOnItemClickListener((parent, view, position, id) -> {
            Recipe selected = matchingRecipes.get(position);

            StringBuilder ingredientsText = new StringBuilder();

            for (Recipe.Ingredient ingredient : selected.getIngredients()) {
                ingredientsText.append("• ")
                        .append(ingredient.getName())
                        .append(" — ")
                        .append(ingredient.getQuantity())
                        .append(" ")
                        .append(ingredient.getUnit())
                        .append("\n");
            }

            android.content.Intent intent = new android.content.Intent(
                    RecipeSuggestionsActivity.this,
                    RecipeDetailActivity.class
            );

            intent.putExtra(
                    RecipeDetailActivity.EXTRA_RECIPE_NAME,
                    selected.getName()
            );
            intent.putExtra(
                    RecipeDetailActivity.EXTRA_RECIPE_DESCRIPTION,
                    selected.getDescription()
            );
            intent.putExtra(
                    RecipeDetailActivity.EXTRA_RECIPE_INGREDIENTS,
                    ingredientsText.toString()
            );
            intent.putExtra(
                    RecipeDetailActivity.EXTRA_RECIPE_INSTRUCTIONS,
                    selected.getInstructions()
            );

            startActivity(intent);
        });
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

        matchingRecipes.clear();

        for (Recipe recipe : RecipeData.getRecipes()) {
            if (matcher.canMakeRecipe(recipe, pantryItems)) {
                matchingRecipes.add(recipe);
            }
        }

        if (matchingRecipes.isEmpty()) {
            recipeList.setVisibility(View.GONE);
            emptyMessage.setVisibility(View.VISIBLE);
            return;
        }

        List<String> displayNames = new ArrayList<>();

        for (Recipe recipe : matchingRecipes) {
            displayNames.add(
                    recipe.getName() + "\n" + recipe.getDescription());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                displayNames
        ) {
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                TextView card = (TextView) super.getView(
                        position, convertView, parent);

                card.setTextColor(GREEN);
                card.setTextSize(16);
                card.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                card.setGravity(Gravity.CENTER_VERTICAL);
                card.setPadding(20, 20, 20, 20);

                GradientDrawable background = new GradientDrawable();
                background.setColor(WHITE);
                background.setCornerRadius(20);
                background.setStroke(1, Color.rgb(225, 232, 223));
                card.setBackground(background);

                ViewGroup.MarginLayoutParams params =
                        new ViewGroup.MarginLayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                params.setMargins(0, 0, 0, 12);
                card.setLayoutParams(params);

                return card;
            }
        };

        recipeList.setAdapter(adapter);
        recipeList.setVisibility(View.VISIBLE);
        emptyMessage.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        super.onDestroy();
    }
}
